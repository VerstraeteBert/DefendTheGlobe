<template>
  <div class="Lobby">
    <div class="container">
      <md-toolbar class="md-align-center">
        <h1>Defend the Globe</h1>
      </md-toolbar>
      <md-layout md-gutter>
        <md-layout mdFlex="33" class="height-85vh">
          <md-tabs md-fixed>
            <md-tab md-label="Friends">
              <md-list class="list md-double-line">
                <md-list-item v-for='message in messageList'>
                  <div class="md-list-text-container">
                    <span>{{message.user}}</span>
                    <span>{{message.message}}</span>
                  </div>
                </md-list-item>
              </md-list>
              <form action="" v-on:submit="sendMessage">
                <md-input-container class="no-margin">
                  <label>You:</label>
                  <md-input v-model="newMessage" required="required"></md-input>
                  <md-button class="md-icon-button" type="submit">
                    <md-icon>send</md-icon>
                  </md-button>
                </md-input-container>
              </form>
            </md-tab>
            <md-tab md-label="Lobby">
              <!--<md-list class="list md-double-line">-->
                <!--<md-list-item v-for='message in lobbyMessages' v-bind:key="message.message">-->
                  <!--<div class="md-list-text-container">-->
                    <!--<span>{{message.user}}</span>-->
                    <!--<span>{{message.message}}</span>-->
                  <!--</div>-->
                <!--</md-list-item>-->
              <!--</md-list>-->
              <form action="" v-on:submit="sendMessage">
                <md-input-container class="no-margin">
                  <label>You:</label>
                  <md-input v-model="newMessage" required="required"></md-input>
                  <md-button class="md-icon-button" type="submit">
                    <md-icon>send</md-icon>
                  </md-button>
                </md-input-container>
              </form>
            </md-tab>
          </md-tabs>
        </md-layout>

        <md-layout mdFlex="33" class="height-85vh">
          <div v-if="option === 'start'" class="flex column">
            <md-button @click='changeSingle' class="md-raised md-accent margin-40">Singleplayer Mode</md-button>
            <md-button @click='changeMulti' class="md-raised md-accent margin-40">Multiplayer Mode</md-button>
            <md-button @click='highscores' class="md-raised md-accent margin-40">Highscores</md-button>
            <md-button class="md-raised md-accent margin-40">How To Play</md-button>
          </div>
          <div v-if="option === 'single'" class="flex column">
            <md-button @click='' class="md-raised md-accent margin-40">Campaign</md-button>
            <md-button @click='showDifficultyModal' class="md-raised md-accent margin-40">Arcade</md-button>
            <md-button @click='defendTheGlobe' class="md-raised md-accent margin-40">Defender of the globe</md-button>
            <md-button @click='backToStart' class="md-raised md-accent margin-40">Back</md-button>
          </div>
          <div v-if="option === 'multi'" class="flex column">
            <md-button @click='coOpScreen' class="md-raised md-accent margin-40">Co-op</md-button>
            <md-button @click='versusScreen' class="md-raised md-accent margin-40">Versus</md-button>
            <md-button @click='backToStart' class="md-raised md-accent margin-40">Back</md-button>
          </div>
          <div style="width: 100%;">
            <div class="flex row">
              <md-button class="md-raised md-primary width-150">Achievements</md-button>
              <md-button class="md-raised md-primary width-150">Shop</md-button>
            </div>
            <div class="flex row">
              <md-button class="md-raised md-primary width-150">Inventory</md-button>
              <md-button class="md-raised md-primary width-150">Clan</md-button>
            </div>
            <div class="flex row">
              <a href="https://twitter.com/intent/tweet?url=http%3A%2F%2Flocalhost%3A1337%2F%23%2F&text=wow%20this%20game%20is%20amazing"><md-button style="width: 200px;" class="md-raised md-primary twitter">Share to twitter</md-button></a>
              <a href="https://www.facebook.com/sharer/sharer.php?u=http%3A//localhost%3A1337/%23/"><md-button style="width: 200px;" class="md-raised md-primary fb">Share to facebook</md-button></a>
            </div>
          </div>
        </md-layout>
        <modal name="difficultyModal" :clickToClose=false height="auto" width="550" :scrollable="true">
          <section class="selectDifficulty flex">
            <h1 id="diffTitle">Select difficulty</h1>
            <md-button class="md-raised md-primary diffButton md-accent" v-on:click="gamePage('Easy')">Easy</md-button>
            <md-button class="md-raised md-primary diffButton md-accent" v-on:click="gamePage('Medium')">Medium</md-button>
            <md-button class="md-raised md-primary diffButton md-accent" v-on:click="gamePage('Hard')">Hard</md-button>
            <md-button class="text-center md-raised md-primary closeBtn" v-on:click="hideDifficultyModal">Back</md-button>

          </section>
        </modal>
        <md-layout mdFlex class="height-85vh scrollbar">
            <md-tabs md-fixed>
              <md-tab id="friends" mdLabel="Friends">
                <md-list class="list">
                  <md-list-item v-for="user in playerList" v-bind:key="user">
                    {{user}}
                    <md-button v-on:click="requestGame">Invite</md-button>
                  </md-list-item>
                </md-list>
              </md-tab>

              <md-tab id="players" mdLabel="Players">
                <md-list class="list">
                  <md-list-item v-for="user in playerList" v-bind:key="user">
                    {{user}}
                    <md-button>Invite</md-button>
                  </md-list-item>
                </md-list>
              </md-tab>

              <md-tab id="clan" mdLabel="Clan">
                <md-list class="list">
                  <md-list-item v-for="user in playerList" v-bind:key="user">
                    {{user}}
                    <md-button>Invite</md-button>
                  </md-list-item>
                </md-list>
              </md-tab>
            </md-tabs>
        </md-layout>
      </md-layout>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'Lobby',
    data() {
      return {
        message: '',
        newMessage: '',
        messageList: [],
        playerList: [],
        clanList: [],
        option: 'start'
      }
    },
    sockets: {},
    methods: {
      showDifficultyModal () {
        this.$modal.show('difficultyModal');
      },
      hideDifficultyModal () {
        this.$modal.hide('difficultyModal');
      },
      changeSingle() {
        this.option = 'single';
      },
      changeMulti() {
        this.option = 'multi';
      },
      backToStart() {
        this.option = 'start';
      },
      gamePage(difficulty) {
        console.log(difficulty);
        this.$router.push({ name: 'GlobalGame', params: {type: 'Arcade', difficulty }});
      },
      defendTheGlobe() {
        this.$router.push({ name: 'GlobalGame', params: {type: 'Defend' }});
      },
      coOpScreen() {
        this.$router.push({ name: 'GlobalGame', params: {type: 'Multi', difficulty: 'Medium' }});
      },
      highscores() {
        this.$router.push('/Highscore')
      },
      versusScreen() {
        console.log("TODO redirect to versus screen");
      },
      sendMessage(e) {
        console.log(self.$session.get("username"));
        e.preventDefault();
        const messageObj = {
          action: "newMessage",
          message: self.newMessage,
          myName: self.$session.get("username")
        };
        console.log(messageObj);
        this.$socket.send(JSON.stringify(messageObj));
        self.newMessage = "";
      },
      updateUserList() {
        this.$socket.send(JSON.stringify({
          action: "getActiveUsers"
        }))
      },
      requestGame() {
        console.log("requesting game")
      }
    },
    beforeCreate() {
      if (!this.$session.exists()) {
        this.$router.push("/");
        this.$toasted.error("NO HACKING ALLOWED XD");
        this.$toasted.show("please login");

      }
    },
    created() {
      self = this;
      setTimeout(() => {
        self.updateUserList();
      }, 3000);
      this.$socket.onmessage = (res) => {
        const obj = JSON.parse(res.data);
        console.log(obj);
        switch (obj.action) {
          case "newMessage":
            self.messageList.push({user: obj.myName, message: obj.message});
            const container = this.$el.querySelector(".md-list");
            container.scrollTop = container.scrollHeight;
            break;
          case "activePlayers":
            self.playerList = obj.playerList;
        }
      };
    }
  }
</script>

<style scoped>
  .flex {
    display: flex;
  }

  .column {
    flex-direction: column;
    width: 100%;
    border: 1px solid black;
  }

  .row {
    justify-content: space-around;
    margin: 40px;
  }

  .margin-40 {
    margin: 40px;
  }

  .width-150 {
    width: 150px;
  }

  .height-85vh {
     max-height: 85vh;
  }

  .scrollbar {
    overflow-y: auto;
  }

  .md-toolbar {
    height: 10vh;
  }

  .md-list {
    min-height: 61vh;
    max-height: 61vh;
    overflow-y: auto;
  }

  .selectDifficulty{
    flex-direction: column;
  }
  .diffButton {
    justify-content: space-around; margin: 10px auto 10px auto;
    width: 60%;
    backgorund-color: pink;
  }
  .closeBtn{
    width: 40%;
    margin: 10px auto 10px auto;
  }
  .fb{
    background-color: #3B5998 !important;
  }
  .twitter{
    background-color: #1dcaff !important;
  }
  #diffTitle{
    margin: 20px auto 20px auto;

  }


</style>
