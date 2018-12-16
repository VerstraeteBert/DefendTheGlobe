import Vue from 'vue'
import VueRouter from 'vue-router'
import VueMaterial from 'vue-material'
import VueSession from 'vue-session'
import Toasted from "vue-toasted"
import VModal from "vue-js-modal"


import Home from '@/components/Home'
import Lobby from '@/components/Lobby'
import Highscore from '@/components/Highscore'
import GlobalGame from '@/components/GlobalGame'

require('vue-material/dist/vue-material.css');

Vue.use(VueRouter);
Vue.use(VueMaterial);
Vue.use(VueSession);
Vue.use(Toasted, {
  duration: 3000,
  icon: "warning"
});
Vue.use(VModal);
Vue.material.registerTheme('default', {
  primary: {
    color: 'grey',
    hue: '700'
  }
})

const route = function (path, name, component, auth) {
  return {path, name, component, meta: {auth}};
}

const router = new VueRouter({
  routes: [
    route('/', 'Home', Home),
    route('/lobby', 'Lobby', Lobby),
    route('/Highscore', 'Highscore', Highscore),
    route('/GlobalGame', 'GlobalGame', GlobalGame)
  ]
})

export default router;
