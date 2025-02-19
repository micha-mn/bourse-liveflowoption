var chart;
var chartLine;
var livepriceData;

   $(document).ready(function () {
      $("#dateFrom, #dateTo").datepicker({
        dateFormat: "yy-mm-dd",
        changeMonth: true,
        changeYear: true,
        minDate: 0, // Prevent past dates
        onSelect: function () {
            updateDateVariables();
        }
    });

    function updateDateVariables() {
        const fromDateValue = $("#dateFrom").val();
        const toDateValue = $("#dateTo").val();

        // Ensure the values are set before formatting
        if (fromDateValue && toDateValue) {
            const fromdate = `${fromDateValue} 00:00:00`;
            const todate = `${toDateValue} 00:00:00`;

            console.log("From Date:", fromdate);
            console.log("To Date:", todate);

            // Use these variables wherever needed
        }
    }
    });
$(function() {
	"use strict";


	const fromdate = "2025-01-31 21:31:02"; //formatDate(combineDateAndTime($("#dateFrom").val(),fromDate));
	const todate = "2025-01-31 21:36:04"; // formatDate(combineDateAndTime($("#dateTo").val(),toDate));

	var ohlcBox = document.getElementById("ohlc-info");

	let dataParam = {
		"fromDate": fromdate,
		"toDate": todate,
		"cryptoCurrencyCode": "BTC",
		"dataType":"NORMAL",
		"period": $(".btn-group .btn.active").text().trim()
		// "cryptoCurrencyCode": $("#currencyDropDown").val(),
	};
	$.ajax({
		type: "POST",
		contentType: "application/json; charset=utf-8",
		url: "/getGraphData",
		data: JSON.stringify(dataParam),
		async: false,
		dataType: 'json',
		timeout: 600000,
		success: function(response) {
			livepriceData=response.dataNormal.data;
			},
				error: function(e) {

			console.log("ERROR : ", e);

		}
	});
	
	$.ajax({
		type: "POST",
		contentType: "application/json; charset=utf-8",
		url: "/getCandleGraphData",
		data: JSON.stringify(dataParam),
		dataType: 'json',
		timeout: 600000,
		success: function(response) {
			console.log(response);

			response.dataCandle.data.forEach(item => {
				item.y = JSON.parse(item.y);
			});

			let data2 = response.dataVolume.data;
			let volumeData = [];
			let volumeColors = []; // ✅ Array to hold bar colors

			// ✅ Loop through candle data and assign volume colors
			response.dataCandle.data.forEach((candle, index) => {
				let open = candle.y[0];
				let close = candle.y[3];
				let volume = data2[index]; // Ensure volume aligns with the candle data

				volumeData.push({
					x: candle.x, // Timestamp
					y: volume    // Volume value
				});

				// ✅ Set color based on bullish/bearish candle
				volumeColors.push(close >= open ? "#00E396" : "#FF4560"); // Green for up, Red for down
			});

			// Extract last candle's OHLC values
			let lastCandleIndex = response.dataCandle.data.length - 1;
			let lastCandle = response.dataCandle.data[lastCandleIndex];

			let open = lastCandle.y[0]; // Open
			let high = lastCandle.y[1]; // High
			let low = lastCandle.y[2]; // Low
			let close = lastCandle.y[3]; // Close

			// Set initial OHLC info
			document.getElementById("date").textContent = lastCandle.x;
			document.getElementById("open").textContent = open;
			document.getElementById("high").textContent = high;
			document.getElementById("low").textContent = low;
			document.getElementById("close").textContent = close;
			$("#ohlc-info").removeClass("d-none");

			var options = {
				series: [{
					data: response.dataCandle.data
				},
				{
					data: livepriceData,
					type:'line'
				}],
				chart: {
					id: 'chart2',
					group: 'candle',
					toolbar: {
						show: true,
						autoSelected: 'pan',
						offsetX: 0,
						offsetY: 0,
						tools: {
							download: false,
							selection: true,
							zoom: true,
							zoomin: true,
							zoomout: true,
							pan: true,
							reset: true | '<img src="/static/icons/reset.png" width="20">',
							customIcons: []
						},
					},
					type: 'candlestick',
					height: 350,
					events: {
						dataPointMouseEnter: function(event, chartContext, { seriesIndex, dataPointIndex, w }) {
							let open = w.globals.seriesCandleO[seriesIndex][dataPointIndex];
							let high = w.globals.seriesCandleH[seriesIndex][dataPointIndex];
							let low = w.globals.seriesCandleL[seriesIndex][dataPointIndex];
							let close = w.globals.seriesCandleC[seriesIndex][dataPointIndex];
							// Update fixed OHLC info box
							document.getElementById("date").textContent = w.config.series[0].data[dataPointIndex].x; //  new Date(w.globals.labels[dataPointIndex]).toLocaleDateString();
							document.getElementById("open").textContent = open;
							document.getElementById("high").textContent = high;
							document.getElementById("low").textContent = low;
							document.getElementById("close").textContent = close;
							$("#ohlc-info").removeClass("d-none");
						},
						dataPointMouseLeave: function(event, chartContext, config) {
							// Hide the box when not hovering (optional)
							// ohlcBox.style.display = "none";
						}
					},
				},
				plotOptions: {
        candlestick: {
            colors: {
                upward: "#00E396",  // ✅ Bullish candles (Green)
                downward: "#FF4560" // ✅ Bearish candles (Red)
            },
            wick: {
                useFillColor: true // ✅ Ensures wicks match the candle body color
            }
        }
    },
				title: {
					align: 'left'
				},
				xaxis: {
					labels: {
						 show: false,
						style: {
							fontSize: '12px',
							fontFamily: 'Helvetica, Arial, sans-serif',
							fontWeight: 400,
							cssClass: 'fill-white',
						},
					},
					type: 'datetime',
					tickAmount: 6,
					crosshairs: {
						show: true,  // ✅ Fix: Ensure the vertical line appears
						width: 1,
						position: "front",
						stroke: {
							color: "#ffffff",
							width: 1,
							dashArray: 3
						}
					},
					axisTicks: { show: false },  // ✅ Remove extra minor ticks
					axisBorder: { show: true }   // Keep only main labels
					, tooltip: { enabled: false },
				},
				yaxis: {
					labels: { 
						style: {
							fontSize: '12px',
							fontFamily: 'Helvetica, Arial, sans-serif',
							fontWeight: 400,
							cssClass: 'fill-white',
						},
					},
					crosshairs: {
						show: true,  // ✅ Fix: Ensure the vertical line appears
						width: 1,
						position: "front",
						stroke: {
							color: "#ffffff",
							width: 1,
							dashArray: 3
						}
					},
					tooltip: { enabled: true },// Show price tooltip on Y-axis

				},
				tooltip: {
					enabled: true,  // ✅ Must be enabled for vertical hover line to work
					custom: function({ series, seriesIndex, dataPointIndex, w }) {
						return '';  // ✅ Hide tooltip content (no number bubble)
					},
					style: {
						fontSize: '0px'  // ✅ Hide tooltip text
					},
					marker: {
						show: false  // ✅ No tooltip markers
					},
					y: {
						show: false  // ✅ No Y-axis tooltip
					},
					x: {
						show: false  // ✅ No X-axis tooltip
					}
				},
				grid: {
					show: true,
					borderColor: '#3d4258',
					strokeDashArray: 0,
					position: 'back',
					xaxis: {
						lines: {
							show: true
						}
					},
					yaxis: {
						lines: {
							show: true
						}
					},
				},
				
			};

			chart = new ApexCharts(document.querySelector("#main-chart"), options);
			chart.render();


			var optionsLine = {
				series: [{
					name: 'volume',
					type: 'bar',
					data: data2
				}],
				chart: {
					id: 'chart1',
					height: 130,
					type: 'bar',
					group: 'candle',
					toolbar: {
						show: false,
					}
				},
				colors: volumeColors,
				plotOptions: {
					bar: {
						distributed: true,
					}
				},
				xaxis: {
					type: 'datetime',
					tooltip: {
						enabled: false
					},
					labels: {
						style: {
							fontSize: '12px',
							fontFamily: 'Helvetica, Arial, sans-serif',
							fontWeight: 400,
							cssClass: 'fill-white',
						},
					},
				},
				yaxis: {
					labels: {
							formatter: function(value) {
			                return value.toFixed(2); 
			            },
						style: {
							fontSize: '12px',
							fontFamily: 'Helvetica, Arial, sans-serif',
							fontWeight: 400,
							cssClass: 'fill-white',
						},
					},
				},
				dataLabels: {
					enabled: false,
				},
				tooltip: {
					enabled: true, 
					shared: true, // ✅ Ensures tooltips appear together on both charts
        			intersect: false ,// ✅ Ensures hover aligns on the same date // ✅ Must be enabled for vertical hover line to work
					custom: function({ series, seriesIndex, dataPointIndex, w }) {
						return '';  // ✅ Hide tooltip content (no number bubble)
					},
					style: {
						fontSize: '0px'  // ✅ Hide tooltip text
					},
					marker: {
						show: false  // ✅ No tooltip markers
					},
					y: {
						show: false  // ✅ No Y-axis tooltip
					},
					x: {
						show: false  // ✅ No X-axis tooltip
					}
				},
				   legend: {
			          show: false
			        },
			};

			chartLine = new ApexCharts(document.querySelector("#chart-line"), optionsLine);
			chartLine.render();


			$("#loading-spinner").hide();

		},
		error: function(e) {

			console.log("ERROR : ", e);

		}
	});



});



function changeTimeframe(timeframe) {
	// Remove 'active' class from all buttons and add it to the clicked one
	document.querySelectorAll(".btn-group .btn").forEach(btn => btn.classList.remove("active"));
	event.target.classList.add("active");

	// Update chart data with the selected timeframe
	updateChart(timeframe);
}

function updateChart(timeframe) {

	const fromdate = "2025-02-15 00:00:00"; //formatDate(combineDateAndTime($("#dateFrom").val(),fromDate));
	const todate = "2025-02-17 00:00:00"; // formatDate(combineDateAndTime($("#dateTo").val(),toDate));

	var ohlcBox = document.getElementById("ohlc-info");

	let dataParam = {
		"fromDate": fromdate,
		"toDate": todate,
		"cryptoCurrencyCode": "BTC",
		"period": timeframe
	};
	// Show the spinner before the request
	$("#loading-spinner").show();

	$.ajax({
		type: "POST",
		contentType: "application/json; charset=utf-8",
		url: "/getCandleGraphData",
		data: JSON.stringify(dataParam),
		dataType: 'json',
		timeout: 600000,
		success: function(response) {
			console.log(response);
			response.dataCandle.data.forEach(item => {
				item.y = JSON.parse(item.y);
			});

			// Initialize chart only once
			if (!chart) {
				chart = new ApexCharts(document.querySelector("#chart"), {
					series: [{
						data: response.dataCandle.data
					}],
					chart: {
						type: 'candlestick',
						height: 350
					},
					xaxis: { type: 'datetime' },
					yaxis: { tooltip: { enabled: true } }
				});
				chart.render();
			} else {
				chart.updateSeries([{ data: response.dataCandle.data }]);
				$("#loading-spinner").hide();
			}

			if (!chartLine) {
				chartLine = new ApexCharts(document.querySelector("#chart-line"), {
					series: [{
						data: response.dataVolume.data
					}],
					chart: {
						type: 'bar',
						height: 350
					},
					xaxis: { type: 'datetime' },
					yaxis: { tooltip: { enabled: true } }
				});
				chartLine.render();
			} else {
				chartLine.updateSeries([{ data: response.dataVolume.data }]);
				$("#loading-spinner").hide();
			}


		},
		error: function(e) {
			console.log("ERROR:", e);
		}
	});

}
