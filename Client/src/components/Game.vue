<template>
  <div id="game">
    <md-layout class="height" md-vertical-align="center" md-align="center">
      <md-spinner id="loader" :md-size="150" v-if="loading" md-indeterminate class="md-accent" md-align="center"></md-spinner>
      <md-layout id="canvas" md-align="center"></md-layout>
    </md-layout>

    <!--<modal name="gameFinished" :clickToClose="true" height="auto" width="550" :scrollable="true">-->
    <!--<section style="padding: 25px;" class="gameFinished">-->
    <!--<h1 class="text-center">You have finished the current stage.</h1>-->
    <!--<hr/>-->
    <!--<h2 class="text-center">If you want to go to the next level:</h2>-->
    <!--<div class="text-center">-->
    <!--<md-button v-on:click="closeModal" class="md-raised md-primary">Continue</md-button>-->
    <!--</div>-->
    <!--<h2 class="text-center">If you want to quit:</h2>-->
    <!--<div class="text-center"><router-link tag="md-button" class="md-raised md-primary" to="/Lobby">Go to Lobby</router-link></div>-->
    <!--</section>-->
    <!--</modal>-->
  </div>
</template>

<script>
  let myName;
  let self;
  export default {
    name: 'Game',
    data() {
      return {
        p5: null,
        paused: this.parentPaused,
        notStarted: this.parentNotStarted,
        totalLives: 0,
        loading: false,
        jsonResponse: {
          action: "game",
          type: 'Arcade',
          difficulty: '',
          players: [],
        },
        player: {
          name: myName,
          move: '',
        }
      }
    },
    props: [
      'parentPaused',
      'parentNotStarted',
      'parentData'
    ],
    methods: {
      createCanvas() {
        self.p5 = new p5(self.sketch, "game");
        self.$emit('p5', self.p5);
      },
      sketch(p) {
        p.setup = function () {
          let cnv = p.createCanvas(500, 500);
          cnv.parent("#canvas");
          p.frameRate(30);
          //self.start = p.loadSound("./static/Start.wav");

        };
        p.draw = function () {
          p.clear();
          if (self.parentData !== null && self.parentData !== 'undefined') {
            // self.loading = false;
            if (p.keyIsDown(p.LEFT_ARROW)) self.player.move = "left";
            if (p.keyIsDown(p.RIGHT_ARROW)) self.player.move = "right";
            //score
            p.fill(255);
            p.fill(0);
            p.fill(0, 0, 0);
            self.parentData.paddles.forEach((paddle) => {
              p.rect(paddle.x, paddle.y, paddle.dx, paddle.dy);
            });
            self.parentData.balls.forEach((ball) => {
              p.fill(ball.color.r, ball.color.g, ball.color.b);
              p.ellipse(ball.coordinate.x, ball.coordinate.y, ball.radius * 2, ball.radius * 2);
            });
            self.parentData.bricks.forEach((brick) => {
              p.fill(brick.color.r, brick.color.g, brick.color.b);
              p.rect(brick.x, brick.y, brick.dx, brick.dy);
            });
            if (self.parentData.effects) {
              self.parentData.effects.forEach((effect, i) => {
                p.fill(effect.color.red, effect.color.green, effect.color.blue);
                p.ellipse(effect.x, effect.y, effect.radius * 2, effect.radius * 2);
                if (document.getElementsByClassName("effect" + i).length === 0) {
                  self.$toasted.info("A new effect has spawned!\nTry to catch it!", {
                    duration: 5000,
                    className: "effect" + i,
                    position: "top-left"
                  });
                }
              });

            }
            if (self.parentData.ceiling) {
              p.fill(0, 0, 0);
              p.rect(self.parentData.ceiling.x, self.parentData.ceiling.y, self.parentData.ceiling.dx, self.parentData.ceiling.dy);
            }
            self.parentData.players.forEach((player, i) => {
              self.totalLives += player.lives;
              p.text(`${player.name}: ${player.lives} lives left & score: ${player.score}`, 10, 20 * (i + 1));

            });
            if (self.parentData.bricks.length === 0) {
              // console.log("ik geraak hier..");
              self.$parent.pauseGame();
              p.clear();
              // p.noLoop();
              p.textSize(32);
              p.text("Game Finished", 100, 250);
              p.textSize(16);
              p.text("Either press space or press start game to go to the next level.", 30, 280);
              self.newGame();
            }
            //handle death
            if (self.totalLives === 0) {
              p.clear();
              p.noLoop();
              p.fill(0);
              p.textSize(32);
              p.textAlign(p.CENTER, p.CENTER);
              p.text("GAME OVER!", 240, 250);
              p.textSize(16);
              self.parentData.players.forEach((player, i) => p.text(`${player.name} scored: ${player.score}`, 250 + i * 5, 280));
              self.$parent.pauseGame();
              setTimeout(() => self.$parent.showHighscorePrompt(), 2000);
            } else if(self.parentData.bricks.length > 0) {
              self.jsonResponse.type = "update";
              self.$socket.send(JSON.stringify(self.jsonResponse));
              self.player.move = '';
              self.totalLives = 0;
            }

          } else {
            p.textSize(24);
            p.text("LOADING", 200, 250);
            // self.loading = true;
          }
        }
      },
      newGame() {
        this.jsonResponse.type = 'Arcade';
        this.$parent.notStarted = true;
        this.$parent.paused = false;
        this.$parent.data = null;
      }
    },
    beforeCreate() {
      if (!this.$session.exists()) {
        this.$router.push("/");
      }
    },
    created() {
      self = this;
      self.paused = false;
      self.player.name = this.$session.get('username');
      if (this.$route.params.difficulty != null) {
        this.jsonResponse.difficulty = this.$route.params.difficulty;
        console.log(this.$route.params.difficulty);
      }
    },
    mounted() {
      this.jsonResponse.players.push(this.player);
      this.$emit("jsonResponse", this.jsonResponse);
      console.log(this.$route.params.difficulty);
    },
    beforeDestroy() {
      this.$parent.resetGame();
    }
  }
</script>

<style>

  #game, .height {
    height: 90vh;
  }

  input[type="text"]{
    width: 150px;
    height: 20px;
    margin: 0 auto 20px;
    display:block;
  }

</style>
