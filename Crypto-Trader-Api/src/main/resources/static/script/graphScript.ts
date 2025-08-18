//=================================-Imports-==================================
import {
    Chart,
    registerables
} from 'chart.js';
import 'chartjs-adapter-date-fns';
import zoomPlugin from 'chartjs-plugin-zoom';
import {PortfolioHistories} from "./PortfolioHistories";
//================================-Init-Load-=================================
Chart.register(...registerables);
Chart.register(zoomPlugin);
//=============================-Client-Functions-=============================

//-------------------------Build-Portfolio-Pie-Chart--------------------------
export function buildPortfolioPieChart(canvasElement: HTMLCanvasElement): void {
    //----------------------------Build-Chart---------------------------------
    new Chart(canvasElement, {
        type: 'pie',
        data: {
            labels: ['BTC', 'ETH', 'SOL', 'ADA', 'SHIB', 'XRP'],
            datasets: [{
                label: 'Portfolio Distribution',
                data: [20, 20, 20, 20, 10, 10],
                backgroundColor: ['hsl(210, 45%, 80%)', 'hsl(210, 45%, 75%)', 'hsl(210, 45%, 70%)', 'hsl(210, 45%, 65%)', 'hsl(210, 45%, 60%)', 'hsl(210, 45%, 55%)'],
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                title: {
                    display: true,
                    text: 'Portfolio'
                },
                legend: {
                    labels: {
                        color: '#000'
                    }
                }
            }
        }
    });
}
//--------------------Build-Portfolio-Total-Value-History---------------------
export function buildPortfolioTotalValueHistoryChart(canvasElement: HTMLCanvasElement,
                                                     portfolioHistories: PortfolioHistories): void {
    const xValues: string[] = portfolioHistories.getDates;
    const yValues: number[] = portfolioHistories.getTotalValues;
    const minValue: number = Math.min(...yValues);
    const maxValue: number = Math.max(...yValues);
    //----------------------------Build-Chart---------------------------------
    new Chart(canvasElement, {
        type: 'line',
        data: {
            labels: xValues,
            datasets: [{
                label: 'Portfolio Total Value History',
                data: yValues.map((value: number, index: number): {x: string, y: number} => ({
                    x: xValues[index],
                    y: value
                })),
                fill: false,
                borderColor: 'rgb(75, 192, 192)',
                tension: 0.1
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                title: {
                    display: true,
                    text: 'Portfolio Total Value History'
                },
                legend: {
                    labels: {
                        color: '#000' // TODO: Try out other colors like sunbeam yellow.
                    }
                },
                zoom: {
                    pan: {
                        enabled: true,
                        mode: 'xy'
                    },
                    zoom: {
                        wheel: {
                            enabled: true,
                        },
                        pinch: {
                            enabled: true
                        },
                        mode: 'xy'
                    }
                }
            },
            scales: {
                x: {
                    type: 'timeseries',
                },
                y: {
                    suggestedMin: minValue - minValue * 0.001,
                    suggestedMax: maxValue + maxValue * 0.001
                }
            }
        }
    });
}
