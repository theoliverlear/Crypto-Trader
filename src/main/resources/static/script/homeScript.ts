import {
    Chart,
    BarController,
    CategoryScale,
    LinearScale,
    BarElement // Add this import
} from 'chart.js';
import {loadPage} from "./globalScript";

// Register the BarElement as well
Chart.register(BarController, CategoryScale, LinearScale, BarElement);


let myChart: HTMLElement = document.getElementById("chart-div");

new Chart((myChart as HTMLCanvasElement), {
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

        }
    }
});
let featureTable: HTMLElement = document.getElementById('feature-table');

function shadowPerspective(): void {
    let scrollPosition = ((window.scrollY / (document.body.scrollHeight - window.innerHeight)) * -1) + 0.8;
    featureTable.style.boxShadow = `#2c4557 calc(((0.5vh + 0.5vw) / 2) + 0.5em) calc((((1vh + 1vw) / 2) + 1em) * ${scrollPosition}) 0 0`;
}
if (loadPage(document.body, 'home')) {
    window.addEventListener('scroll', shadowPerspective);
}