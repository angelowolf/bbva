<template>
  <div class="hello">
    <h1>Fondos Total</h1>
    <chart-linea :labels="labels" :datasets="datasets" v-if="mostrar"></chart-linea>
    <h1>Fondos Variacion Mercado</h1>
    <chart-linea :labels="labels2" :datasets="datasets2" v-if="mostrar2"></chart-linea>
    <h1>Fondos Cantidad de cada uno</h1>
    <chart-linea :labels="labels3" :datasets="datasets3" v-if="mostrar3"></chart-linea>
  </div>
</template>

<script>
import ChartLinea from './ChartLinea'
export default {
  components: {
    ChartLinea
  },
  name: 'ChartFondo',
  data () {
    return {
      datasets: [],
      labels: [],
      mostrar: false,
      datasets2: [],
      labels2: [],
      mostrar2: false,
      datasets3: [],
      labels3: [],
      mostrar3: false
    }
  },
  created () {
    this.axios.get('http://localhost:8080/bbva/api/fondo/total')
      .then(response => {
        this.datasets = response.data.datasets.map(d => {
          d.fill = false
          d.borderColor = d.backgroundColor
          return d
        })
        this.labels = response.data.labels
        this.mostrar = true
      })
    this.axios.get('http://localhost:8080/bbva/api/fondo/mercado')
      .then(response => {
        this.datasets2 = response.data.datasets.map(d => {
          d.fill = false
          d.borderColor = d.backgroundColor
          return d
        })
        this.labels2 = response.data.labels
        this.mostrar2 = true
      })
    this.axios.get('http://localhost:8080/bbva/api/fondo/disponible')
      .then(response => {
        this.datasets3 = response.data.datasets.map(d => {
          d.fill = false
          d.borderColor = d.backgroundColor
          return d
        })
        this.labels3 = response.data.labels
        this.mostrar3 = true
      })
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h1, h2 {
  font-weight: normal;
}

ul {
  list-style-type: none;
  padding: 0;
}

li {
  display: inline-block;
  margin: 0 10px;
}

a {
  color: #42b983;
}
</style>
