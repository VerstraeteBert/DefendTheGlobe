<template>
  <div class="StarField">
    <canvas ref='canvas' id="canvas"></canvas>
  </div>
</template>

<script>

import Star from '../assets/js/star';
import Canvas from '../assets/js/canvas';
let canvas;
let i;

export default {
  name: 'StarField',
  data() {
    return {
      stars: [],
    }
  },
  methods: {
    setup() {
      canvas = new Canvas(this.$refs.canvas);
      canvas.translate(canvas.width/2, canvas.height/2)

      for (let i = 0; i < 500; i++) {
        this.stars.push(new Star(
          Math.random() * canvas.width - canvas.width / 2,
          Math.random() * canvas.height - canvas.height / 2,
          Math.random() * (canvas.width / 2)
      ))}
    },
    run() {
      i = setInterval(() => {
        for (let i = 0; i < this.stars.length; i++) {
          this.stars[i].update();

          if (this.stars[i].needsReset()) {
            this.stars[i].reset(
              Math.random() * canvas.width - canvas.width / 2,
              Math.random() * canvas.height - canvas.height / 2,
              Math.random() * (canvas.width / 2)
            )
          }
        }
      }, 1000/20);
    },
    drawCanvas() { //FIXME beter naamgeving
      canvas.draw(() => {
        for (let i = 0; i < this.stars.length; i++) {
          this.stars[i].draw(canvas);
        }
      })
    },
    respCanvas() {
      console.log(document.body.clientWidth, document.body.clientHeight);
      canvas.resize(document.body.clientWidth, document.body.clientHeight)
    }
  },
  sockets: {},
  created() {
    window.addEventListener('resize', this.respCanvas);
  },
  mounted() {

    this.setup();
    this.run();
    this.drawCanvas();
  },
  beforeDestroy() {
    clearInterval(i);
  }
}
</script>

<style scoped lang="css">
canvas {
  position: absolute;
  top: 0;
  left: 0;
}
</style>
