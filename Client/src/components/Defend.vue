<template>
  <div id="Game">

    <md-layout class="height" md-vertical-align="center" md-align="center">
      <md-layout id="canvas" md-align="center"></md-layout>
    </md-layout>
  </div>
</template>

<script>

  let self;

  export default {
    name: 'Defend',
    data() {
      return {
        p5: null,
        paused: this.parentPaused,
        notStarted: this.parentNotStarted,
        jsonResponse: {
          action: 'game',
          type: 'Defend',
          players: [],
        },
        player: {
          name: '',
          move: '',
          score: 0,
          lives: 3,
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
          self.img = p.loadImage("./static/GLOBE.png");
        };
        p.draw = function () {
          p.clear();
          if (p.keyIsDown(p.LEFT_ARROW)) self.jsonResponse.players[0].move = 'left'
          if (p.keyIsDown(p.RIGHT_ARROW)) self.jsonResponse.players[0].move = 'right'
          if (self.parentData != null && self.parentData !== 'undefined') {
            self.parentData.bricks.forEach((brick) => {
              p.fill(brick.color.r, brick.color.g, brick.color.b);
              p.rect(brick.x, brick.y, brick.dx, brick.dy);
            });
            p.fill(0, 255, 0);
            self.parentData.paddles.forEach(paddle => {
              p.arc(paddle.x, paddle.y, paddle.radius * 2, paddle.radius * 2, paddle.start, paddle.start + paddle.extent);
              p.fill(80);
              p.arc(paddle.x, paddle.y, paddle.radius * 2 - 20, paddle.radius * 2 - 20, paddle.start, paddle.start + paddle.extent);
              p.fill(0, 0, 255);
            })
            p.ellipse(self.parentData.theGlobe.coordinate.x, self.parentData.theGlobe.coordinate.x, self.parentData.theGlobe.radius * 2, self.parentData.theGlobe.radius * 2);
            p.fill(255, 0, 0);
            self.parentData.balls.forEach(ball => p.ellipse(ball.coordinate.x, ball.coordinate.y, ball.radius * 2, ball.radius * 2))
            //p.ellipse(self.gameData.ball.coordinate.x, self.gameData.ball.coordinate.y, self.gameData.ball.radius * 2, self.gameData.ball.radius * 2);
            self.$socket.send(JSON.stringify(self.jsonResponse));
            self.jsonResponse.players[0].move = '';
            if (self.parentData.players) {
              self.player.score = self.parentData.players[0].score;
              self.player.lives = self.parentData.players[0].lives;
            }
            if (self.parentData.bricks.length === 0) {
              self.paused = true;
              p.clear();
              // p.noLoop();
              p.fill(255);
              p.textSize(32);
              p.text("Game Finished", 145, 150);
              p.textSize(16);

              p.text("Either press space or press start game to go to the next level.", 25, 350);
            }
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
            if (self.player.lives == 0) {
              p.clear();
              p.noLoop();
              p.textSize(32);
              p.textAlign(p.CENTER, p.CENTER);
              p.text("GAME OVER!", 240, 150);
              p.textSize(16);
              self.parentData.players.forEach((player, i) => {
                p.text(`${player.name} scored: ${player.score}`, 250 + i * 5, 350);
                self.player.score = player.score;
              });
              self.$parent.pauseGame();
              setTimeout(() => self.$parent.showHighscorePrompt(), 2000);
            }

          p.image(self.img, 250 - 50, 250 - 50, 100, 100);
          }
          else {
             p.text("LOADING", 250, 250);
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
    },
    mounted() {
      this.jsonResponse.players.push(this.player);
      this.$emit("jsonResponse", this.jsonResponse);
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

</style>
