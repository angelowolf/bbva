import Vue from 'vue'
import Router from 'vue-router'
import ChartFondo from '@/components/ChartFondo'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Fondos',
      component: ChartFondo
    }
  ]
})
