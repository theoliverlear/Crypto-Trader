new Chart(document.getElementById("chart-div"), {
  type: 'bar',
  data: {
    labels: ['ETH', 'BTC', 'SHIB', 'ADA', 'XRP', 'SOL'],
    datasets: [{
      label: 'Year to Date Percentage Increase',
      data: [10, 15, 16, 22, 25, 40],
      backgroundColor: ['hsl(210, 45%, 80%)', 'hsl(210, 45%, 75%)', 'hsl(210, 45%, 70%)', 'hsl(210, 45%, 65%)', 'hsl(210, 45%, 60%)', 'hsl(210, 45%, 55%)'],
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
