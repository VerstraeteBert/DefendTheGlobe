// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App.vue'
import VueNativeWebSocket from 'vue-native-websocket'
import router from './router'
require('./assets/js/p5');


Vue.use(VueNativeWebSocket, "ws://localhost:8080/AtariBreakout/ws", {format: 'json'});

Vue.config.productionTip = false

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  template: '<App/>',
  components: { App }
});
