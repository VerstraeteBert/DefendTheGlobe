<template>
  <div id="Multi">
    <md-layout class="height" md-vertical-align="center" md-align="center">
      <md-spinner id="loader" :md-size="150" v-if="loading" md-indeterminate class="md-accent"></md-spinner>
      <md-layout id="canvas" md-align="center"></md-layout>
    </md-layout>
  </div>
</template>

<script>
  let self;

  export default {
    name: 'Multi',
    data() {
      return {
        p5: null,
        paused: this.parentPaused,
        notStarted: this.parentNotStarted,
        totalLives: 0,
        loading: false,
        difficulty: '',
        jsonResponse: {
          action: "game",
          type: 'Coop',
          players: [],
        },
        player: {
          name: '',
          move: '',
        },
        player1: {
          name: 'joske',
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
        };
        p.draw = function () {
          p.clear();
          console.log(self.parentData);
          if (self.parentData !== null && typeof self.parentData !== 'undefined') {
            // self.loading = false;
            if (p.keyIsDown(p.LEFT_ARROW)) self.jsonResponse.players[0].move = 'left';
            if (p.keyIsDown(p.RIGHT_ARROW)) self.jsonResponse.players[0].move = 'right';
            if (p.keyIsDown(p.CONTROL)) self.jsonResponse.players[1].move = 'left';
            if (p.keyIsDown(p.ALT)) self.jsonResponse.players[1].move = 'right';
            p.fill(0, 0, 0);
            p.fill(255);
            p.fill(0);
            p.fill(0, 0, 0);
            if (typeof self.parentData.paddles !== 'undefined' && self.parentData.paddles != null) {
              self.parentData.paddles.forEach((paddle) => {
                p.rect(paddle.x, paddle.y, paddle.dx, paddle.dy);
              });
            }
            if (typeof self.parentData.balls !== 'undefined' && self.parentData.balls != null) {
              self.parentData.balls.forEach((ball) => {
                p.fill(ball.color.r, ball.color.g, ball.color.b);
                p.ellipse(ball.coordinate.x, ball.coordinate.y, ball.radius * 2, ball.radius * 2);
              });
            }
            if (typeof self.parentData.bricks !== 'undefined' && self.parentData.bricks != null) {
              self.parentData.bricks.forEach((brick) => {
                p.fill(brick.color.r, brick.color.g, brick.color.b);
                p.rect(brick.x, brick.y, brick.dx, brick.dy);
              });
            }

            if (typeof self.parentData.effects !== 'undefined' && self.parentData.effects != null) {
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
            if (typeof self.parentData.ceiling !== 'undefined' && self.parentData.ceiling != null) {
              p.fill(0, 0, 0);
              p.rect(self.parentData.ceiling.x, self.parentData.ceiling.y, self.parentData.ceiling.dx, self.parentData.ceiling.dy);
            }
            if (typeof self.parentData.players !== 'undefined' && self.parentData.players != null) {
              self.parentData.players.forEach((player, i) => {
                self.totalLives += player.lives;
                p.text(`${player.name}: ${player.lives} lives left & score: ${player.score}`, 10, 20 * (i + 1));

              });
            }

            if (typeof self.parentData.bricks !== 'undefined' && self.parentData.bricks.length === 0) {
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
              if (typeof self.parentData !== 'undefined' && self.parentData != null) {
                self.parentData.players.forEach((player, i) => p.text(`${player.name} scored: ${player.score}`, 250, 280 + i * 10));
              }
              self.$parent.pauseGame();
              setTimeout(() => self.$parent.showHighscorePrompt(), 2000);
            } else if(self.parentData.bricks.length > 0) {
              self.jsonResponse.type = "update";
              self.$socket.send(JSON.stringify(self.jsonResponse));
              self.player.move = '';
              self.player1.move = '';
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
    created() {
      self = this;
      self.paused = false;
      this.player.name = this.$session.get('username');
      if (this.$route.params.difficulty != null) {
        this.jsonResponse.difficulty = this.$route.params.difficulty;
        console.log(this.$route.params.difficulty);
      }
    },
    mounted() {
      this.jsonResponse.players.push(this.player, this.player1);
      this.$emit('jsonResponse', this.jsonResponse);
    },
    beforeDestroy() {
      this.$socket.send(JSON.stringify({action: 'stop'}))
      this.$parent.resetGame();
    }
  }
</script>

<style>

  #Multi, .height {
    height: 90vh;
  }

  #loader {
    position: absolute;
    top: 47vh;
    left: 44vw;
  }

</style>
