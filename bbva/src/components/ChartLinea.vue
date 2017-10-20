<script>
import { Line } from 'vue-chartjs'

export default Line.extend({
  props: {
    datasets: {
      default: [],
      type: Array
    },
    labels: {
      default: [],
      type: Array
    }
  },
  name: 'ChartLinea',
  mounted () {
    this.renderChart({
      labels: this.labels,
      datasets: this.datasets
    }, {responsive: true, maintainAspectRatio: false, fill: false})
  }
})
</script>
