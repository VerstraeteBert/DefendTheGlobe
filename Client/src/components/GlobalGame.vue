<template>
  <div id="GlobalGame">
    <md-toolbar id="toolbar" style="height: 10vh;">
      <md-layout md-flex="25">
        <md-button v-on:click="resetGame" class="md-raised buttonwidth md-accent">
          Back To Lobby
        </md-button>
      </md-layout>
      <md-layout md-flex="50" md-align="center">
        <h1>Defend the Globe</h1>
      </md-layout>
      <md-layout md-flex="25" md-align="end">
        <md-button v-if="paused || notStarted" v-on:click="resumeHandler" class="md-raised buttonwidth md-accent">
          Start Game
        </md-button>
        <md-button v-else v-on:click="pauseHandler" class="md-raised buttonwidth md-accent">
          Pause Game
        </md-button>
      </md-layout>
    </md-toolbar>

    <Game md-flex="100" class="gameComponent" ref="Arcade" @p5="setP5" @jsonResponse="setJsonresponse" v-if="game === 'Arcade'" v-bind:parent-data="data" v-bind:parent-paused="paused" v-bind:parent-not-started="notStarted"></Game>
    <Multi md-flex="100" class="gameComponent" ref="Multi" v-if="game === 'Multi'" @p5="setP5" @jsonResponse="setJsonresponse" v-bind:parent-data="data" v-bind:parent-paused="paused" v-bind:parent-not-started="notStarted"></Multi>
    <Defend md-flex="100" class="gameComponent" ref="Defend" v-if="game === 'Defend'" @p5="setP5" @jsonResponse="setJsonresponse" v-bind:parent-data="data" v-bind:parent-paused="paused" v-bind:parent-not-started="notStarted"></Defend>

    <modal name="pauseModal" :clickToClose=false height="auto" width="550" :scrollable="true">
      <section class="pauseGame">
        <h1 class="text-center">Your game is paused</h1>

        <h2 class="text-center">These are our tips and facts on staying hydrated.</h2>

        <ul>
          <li>Not staying hydrated might make you lightheaded and dizzy.</li>
          <li>It can be the difference maker of not using an powerup correctly</li>
          <li>Not drinking water will make you sleepy</li>
          <li>Staying hydrated will make you win Defend the Globe</li>
        </ul>
        <div class="text-center">
          <md-button v-on:click="resumeHandler">Continue</md-button>
        </div>
      </section>
    </modal>

    <modal @before-close="redirectToLobby" name="promptHighscores" :clickToClose=true height="auto" width="550" :scrollable="true">
      <section class="promptHighscores text-center">
        <h1>You died!</h1>
        <h2>Submit your score to our highscores</h2>
        <form>
          <input type="text" name="nameToSubmit" ref="nameToSubmit" placeholder="Enter your name...">
          <md-button  type="submit" class="md-primary md-raised buttonwidth" v-on:click="saveToHighscores">Continue</md-button>
        </form>
      </section>
    </modal>
  </div>

</template>

<script>
import Game from './Game';
import Multi from './Multi';
import Defend from './Defend';

let self;
export default {
  name: 'GlobalGame',
  data() {
    return {
      game: '',
      jsonResponse: '',
      data: null,
      p5: null,
      paused: false,
      notStarted: true,
    }
  },
  components: {
    Game,
    Multi,
    Defend
  },
  methods: {
    setP5(p5) {
      this.p5 = p5;
    },
    setJsonresponse(jsonResponse) {
      console.log(jsonResponse);
      this.jsonResponse = jsonResponse;
    },
    pauseHandler(e) {
      e.preventDefault();
      console.log("pausing");
      this.pauseGame();
      this.$modal.show('pauseModal', {
        title: 'Alert!',
        text: 'You are too awesome'
      });
    },
    resumeHandler(e) {
      e.preventDefault();
      console.log("resuming / starting");
      this.startGame();
      this.$modal.hide('pauseModal');
      this.notStarted = false;
    },
    startGame() {
      if (document.querySelector("#canvas").childNodes[0]) {
        document.querySelector("#canvas").childNodes[0].remove();
      }
      if (this.paused) {
        this.$socket.send(JSON.stringify({action: "resume"}));
        this.paused = false;
      }
      if (this.notStarted) {
        this.$socket.send(JSON.stringify(this.jsonResponse));
        this.jsonResponse.type = 'update';
      }
      this.$refs[self.game].createCanvas();
    },
    pauseGame() {
      this.clearFrameRate();
      self.paused = true;
      console.log("sending pause game");
      self.$socket.send(JSON.stringify({action: "pause"}));
      console.log("ispaused: ", self.paused);

    },
    resetGame() {
      if (self.started) this.clearFrameRate();
      console.log("sending stopGame");
      this.$socket.send(JSON.stringify({action: "stop"}));
      this.$router.push("/Lobby");
    },
    clearFrameRate() {
      if (this.p5 != null) {
        this.p5.noLoop();
        this.p5 = null;
      }
    },
    keyEvents(e) {
      if (e.keyCode === 32 && this.paused) this.resumeHandler(e);
      else if (e.keyCode === 32 && !this.paused) this.pauseHandler(e);
    },
    redirectToLobby(){
      self.$router.push("/lobby");
    },
    showHighscorePrompt(){
      this.$modal.show('promptHighscores');
    },
    saveToHighscores(e){
      e.preventDefault();
      let nameToSubmit = this.$refs.nameToSubmit.value;
      if (nameToSubmit !== '' && nameToSubmit !== null) {
        fetch(`/AtariBreakout/highScore?name=${nameToSubmit}&score=${self.data.players[0].score}&type=${this.$route.params.type}`, {
          method: "put"
        }).then((res) => {
          self.$toasted.show("Your score has been saved!");
          self.$toasted.show("Check it out in the higschores", {
            action: {
              text: "Take a look!",
              onClick: (e, toastObj) => {
                toastObj.goAway(0);
                self.$router.push("/highscore");
              }
            }
          });
        });
      }
      self.$router.push("/lobby");
    }
  },
  beforeCreate() {
    if (!this.$session.exists()) {
      this.$router.push("/");
      this.$toasted.error("Please login first..");
    }
  },
  created() {
    self = this;
    this.$socket.onmessage = e => self.data = JSON.parse(e.data);
    if (this.$route.params.type != null) {
      console.log(this.$route.params);
      self.game = this.$route.params.type;

    }
  },
  mounted() {
    window.addEventListener('keyup', this.keyEvents)
  },
  beforeDestroy() {
    window.removeEventListener('keyup', this.keyEvents);
    this.resetGame();
    this.clearFrameRate();
  }
}
</script>

<style>

  #GlobalGame {
    height: 100vh;
    background: url('../../static/nasa-89125.jpg');
    background-size: cover;
    background-repeat: no-repeat;
  }

  #toolbar {
    opacity: 0.85;
  }

  #defaultCanvas0 {
    box-shadow: 0px 4px 8px 9px rgba(0, 214, 38, 0.2);
    border-radius: 5px;
  }

  .game {
    display: flex;
    justify-content: center;
    align-items: center;
  }

  .pauseGame {
    padding-left: 20px;
    padding-right: 20px;
  }

  .text-center {
    text-align: center;
    align-items: center;
  }

  button.text-center {
    width: 70px;
    height: 20px;
    color: black;
    margin: 0 auto 20px;
  }

  .buttonwidth {
    width: 150px;
  }

  .flexend {
    display: flex;
    flex-direction: column;
    align-items: flex-end;
  }

</style>
