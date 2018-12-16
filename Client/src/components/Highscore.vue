<template>
  <div id="highscores">
    <md-button @click="backToLobby" id="backToLobby" class="md-raised md-accent">Back to lobby</md-button>
    <md-toolbar style="height: 10vh;" class="md-align-center">
      <h1>Defend the Globe</h1>
    </md-toolbar>
    <md-toolbar style="height: 5vh; padding: 10px;" class="md-align-center">
      <h2>Highscores</h2>
    </md-toolbar>
    <md-tabs md-fixed>
      <md-tab id="arcade" md-label="Arcade">
        <table>
          <thead>
            <tr>
              <th>Name</th>
              <th>Type</th>
              <th>Score</th>
              <th>Date</th>
            </tr>
          </thead>

          <tbody>
            <tr v-for="score in highscores.arcade">
              <td>{{score.name}}</td>
              <td>{{score.type}}</td>
              <td>{{score.score}}</td>
              <td>{{score.date}}</td>
            </tr>
          </tbody>
        </table>
      </md-tab>
      <md-tab id="defend" md-label="Defend the Globe">
        <table>
          <thead>
            <tr>
              <th>Name</th>
              <th>Type</th>
              <th>Score</th>
              <th>Date</th>
            </tr>
          </thead>

          <tbody>
            <tr v-for="score in highscores.defend">
              <td>{{score.name}}</td>
              <td>{{score.type}}</td>
              <td>{{score.score}}</td>
              <td>{{score.date}}</td>
            </tr>
          </tbody>
        </table>
      </md-tab>
      <md-tab id="coop" md-label="Co op">
        <table>
          <thead>
            <tr>
              <th>Name</th>
              <th>Type</th>
              <th>Score</th>
              <th>Date</th>
            </tr>
          </thead>

          <tbody>
            <tr v-for="score in highscores.coop">
              <td>{{score.name}}</td>
              <td>{{score.type}}</td>
              <td>{{score.score}}</td>
              <td>{{score.date}}</td>
            </tr>
          </tbody>
        </table>
      </md-tab>
    </md-tabs>
  </div>
</template>

<script>
let self;
export default {
  name: 'highscores',
  data() {
    return {
      highscores: {
        arcade: [],
        defend: [],
        coop: []
      }
    }
  },
  methods: {
    loadHighscores() {
      const options = {
        method: 'POST',
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
      }
      fetch('/AtariBreakout/highScore', options)
        .then(res => res.json())
        .then(res => {
          console.log(res);
          res.forEach(score => {
            if(score.type == 'Arcade') this.highscores.arcade.push(score);
            if(score.type == 'Defend') this.highscores.defend.push(score);
            if(score.type == 'Multi') this.highscores.coop.push(score);
          })
          Object.values(self.highscores).forEach(array => self.sortArrays(array));
        })
    },
    sortArrays(array) {
      array.sort((a, b) => {
        return b.score - a.score;
      })
    },
    backToLobby() {
      this.$router.push('/Lobby');
    }
  },
  created() {
    self = this;
    this.loadHighscores();
  }
}
</script>

<style>
  table {
    width: 100%;
    text-align: center;
  }
  th {
    width: 25%;
    padding: 0;
    border-bottom: 1px solid black;
  }
  th, td {
    padding: 10px 5px;
  }

  #backToLobby {
    position: absolute;
    top: 10px;
    left: 10px;
    z-index: 10;
  }

</style>
