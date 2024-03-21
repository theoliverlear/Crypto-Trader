new Chart(document.getElementById("chart-div"), {
  type: 'bar',
  data: {
    labels: ['ETH', 'BTC', 'SHIB', 'ADA', 'XRP', 'SOL'],
    datasets: [{
      label: 'Year to Date Percentage Increase',
      data: [10, 15, 16, 22, 25, 40],
      backgroundColor: ['hsl(210, 45%, 80%)', 'hsl(210, 45%, 75%)', 'hsl(210, 45%, 70%)', 'hsl(210, 45%, 65%)', 'hsl(210, 45%, 60%)', 'hsl(210, 45%, 55%)'],
      // backgroundColor: ['hsl(46,40%,74%)', 'hsl(46,50%,74%)', 'hsl(46,60%,74%)', 'hsl(46,70%,74%)', 'hsl(46,80%,74%)', 'hsl(46,90%,74%)'],
      borderWidth: 1
    }]
  },
  options: {
    responsive: true,
    maintainAspectRatio: false,
    scales: {
      y: {
        beginAtZero: true
      }
    }
  }
});
let featureTable = document.getElementById('feature-table');

function shadowPerspective() {
  let scrollPosition = ((window.scrollY / (document.body.scrollHeight - window.innerHeight)) * -1) + 0.8;
  featureTable.style.boxShadow = `#2c4557 calc(((0.5vh + 0.5vw) / 2) + 0.5em) calc((((1vh + 1vw) / 2) + 1em) * ${scrollPosition}) 0 0`;
}
window.addEventListener('scroll', shadowPerspective);