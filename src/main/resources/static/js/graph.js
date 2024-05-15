var chart;
var json = {};

var toDate = new Date();
var fromDate = new Date();
fromDate = new Date(fromDate.setHours(fromDate.getHours() - 1));

$(document).ready(function() {
	$("#dateFrom").jqxDateTimeInput({ formatString: "F", showTimeButton: true, width: '325px', height: '40px' });
	$("#dateTo").jqxDateTimeInput({ formatString: "F", showTimeButton: true, width: '325px', height: '40px' });



	$("#dateFrom").val(fromDate);
	$("#dateTo").val(toDate);

	updateDateInputs();


});
function changeDate(type, direction, unit) {
    var amount = direction === 'forward' ? 1 : -1;
    if (unit === 'hour') {
      if (type === 'from') {
        var hours = fromDate.getHours() + amount;
        if (hours < 0) {
          fromDate.setDate(fromDate.getDate() - 1);
          hours += 24;
        } else if (hours >= 24) {
          fromDate.setDate(fromDate.getDate() + 1);
          hours -= 24;
        }
        fromDate.setHours(hours);
      } else {
        var hours = fromDate.getHours() + amount;
        if (hours < 0) {
          fromDate.setDate(fromDate.getDate() - 1);
          hours += 24;
        } else if (hours >= 24) {
          fromDate.setDate(fromDate.getDate() + 1);
          hours -= 24;
        }
        fromDate.setHours(hours);
      }
    } else if (unit === 'day') {
      if (type === 'from') {
        fromDate.setDate(fromDate.getDate() + amount);
      } else {
        fromDate.setDate(fromDate.getDate() + amount);
      }
    }
  updateDateInputs();
	$("#dateFrom").val(fromDate);
	$("#dateTo").val(toDate);
	drawGraph();
  }

function updateDateInputs() {
	document.getElementById('fromDate').value = fromDate.toString();
	document.getElementById('toDate').value = toDate.toString();
}
function drawGraph() {
 
	dataParam = {
		"fromDate": formatDate($("#dateFrom").val()),
		"toDate": formatDate($("#dateTo").val()),
		"dataType": 'normal',
		"cryptoCurrencyCode": 'ENNA',
	};
	$.ajax({
		type: "POST",
		contentType: "application/json; charset=utf-8",
		url: "/getGraphData",
		data: JSON.stringify(dataParam),
		dataType: 'json',
		timeout: 600000,
		success: function(response) {
			console.log(response);
			const jsonArrayWithNumberTimestamp = response.dataNormal.data.map(obj => {
				return {
					...obj,
					x: parseInt(obj.x)
				};
			});
			const jsonArrayWithNumberTimestamp1 = response.dataMin.data.map(obj => {
				return {
					...obj,
					x: parseInt(obj.x)
				};
			});
			const jsonArrayWithNumberTimestamp2 = response.dataMax.data.map(obj => {
				return {
					...obj,
					x: parseInt(obj.x)
				};
			});
			min = Math.min.apply(null, jsonArrayWithNumberTimestamp.map(function(item) {
				return item.y;
			})),
				max = Math.max.apply(null, jsonArrayWithNumberTimestamp.map(function(item) {
					return item.y;
				}));
			const values = addMarginToMinMax(min, max, 10);

			json.series = [{
				name: "ENNA",
				data: jsonArrayWithNumberTimestamp
			},
			{
				name: "MAX",
				data: jsonArrayWithNumberTimestamp2
			}, {
				name: "MIN",
				data: jsonArrayWithNumberTimestamp1
			},];
			json.xaxis= [
			      {
			        // in a datetime series, the x value should be a timestamp, just like it is generated below
			        x: 1715797050,
			        strokeDashArray: 0,
			        borderColor: "#775DD0",
			        label: {
			          borderColor: "#775DD0",
			          style: {
			            color: "#fff",
			            background: "#775DD0"
			          },
			          text: "X Axis Anno Vertical"
			        }
			      },
			      {
			        x: 1715797370,
			        borderColor: "#FEB019",
			        label: {
			          borderColor: "#FEB019",
			          style: {
			            color: "#fff",
			            background: "#FEB019"
			          },
			          orientation: "horizontal",
			          text: "X Axis Anno Horizonal"
			        }
			      }
			    ];
			json.points = [{
				x: 1715796490,
				y: 0.71500000,
				marker: {
					size: 6,
					fillColor: "#ff0000",
					strokeColor: "#ff0000",
					radius: 2
				},
				/*label: {
					borderColor: "#FF4560",
					offsetY: 0,
					style: {
						color: "#ff0000",
					    background: "#ffffff00"
					},

					text: "Point Annotation (XY)"
				}*/
			}
			,
			{
				x: 1715797294,
				y: 0.72100000,
				marker: {
					size: 7,
					fillColor: "#ff0000",
					strokeColor: "#ff0000",
					radius: 2
				},
				/*label: {
					borderColor: "#FF4560",
					offsetY: 0,
					style: {
						color: "#ff0000",
					    background: "#ffffff00"
					},

					text: "Point Annotation (XY)"
				}*/
			}
			];
			chartOptions = getChartOption(json)
			if (chart != null) {
				chart.updateOptions(chartOptions);
			}
			else {
				chart = new ApexCharts(document.querySelector("#chart"), chartOptions);
				chart.render();
			}

			testgraph();


		},
		error: function(e) {

			console.log("ERROR : ", e);

		}
	});
}
function formatDate(inputDate) {
	// Create a Date object from the input string
	const dateObj = new Date(inputDate);

	// Extract date components
	const year = dateObj.getFullYear();
	const month = String(dateObj.getMonth() + 1).padStart(2, '0');
	const day = String(dateObj.getDate()).padStart(2, '0');

	// Extract time components
	const hours = String(dateObj.getHours()).padStart(2, '0');
	const minutes = String(dateObj.getMinutes()).padStart(2, '0');
	const seconds = String(dateObj.getSeconds()).padStart(2, '0');

	// Construct the desired format
	const formattedDate = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
	return formattedDate;
}
function timestampToDate(timestamp) {
	// Convert timestamp to milliseconds
	const milliseconds = timestamp * 1000;

	// Create a new Date object
	const dateObj = new Date(milliseconds);

	// Extract date components
	const year = dateObj.getFullYear();
	const month = String(dateObj.getMonth() + 1).padStart(2, '0');
	const day = String(dateObj.getDate()).padStart(2, '0');

	// Extract time components
	const hours = String(dateObj.getHours()).padStart(2, '0');
	const minutes = String(dateObj.getMinutes()).padStart(2, '0');
	const seconds = String(dateObj.getSeconds()).padStart(2, '0');

	// Construct the formatted date string
	const formattedDate = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
	return formattedDate;
}

function addMarginToMinMax(minValue, maxValue, marginPercentage) {
	const margin = (maxValue - minValue) * (marginPercentage / 100);
	return margin;
}

function getChartOption(json) {
	var options = {
		series: json.series,
		chart: {
			height: 350,
			type: 'line',
			id: 'fb1',
			group: 'trading',
			toolbar: {
				show: true,
			},
			  animations: {
		        enabled: true,
		        easing: 'easeinout',
		        speed: 800,
		        animateGradually: {
		            enabled: true,
		            delay: 150
		        },
		        dynamicAnimation: {
		            enabled: true,
		            speed: 350
		        }
		    }
		},
		dataLabels: {
			enabled: false
		},
		markers: {
			size: [3, 5, 5]
		},
		stroke: {
			curve: 'straight',
			width: 2,
		},
		title: {
			text: '',
			align: 'left'
		},
		xaxis: {
			type: 'datetime',
			labels: {
				//   rotate: -45,
				//   rotateAlways: true,
				formatter: function(value, timestamp, opts) {
					return timestampToDate(timestamp)
				}
			}
		},
		yaxis: {
			labels: {
				minWidth: 75, maxWidth: 75,
			},
			// tickAmount: 6,
			axisBorder: {
				width: 3,
				show: true,
				color: '#ffffff',
				offsetX: 0,
				offsetY: 0
			},
		},
		tooltip: {
			custom: function({ series, seriesIndex, dataPointIndex, w }) {
				return '<div class="arrow_box">' +
					'<span>' + series[seriesIndex][dataPointIndex] + '</span>' +
					'</div>'
			},
			marker: {
				show: true,
			},
		},
		annotations: {
			//points: json.points,
			//xaxis: json.xaxis
			},
	};
	return options;
}
function testgraph(){
	var options = {
          series: [json.series[0]],
          chart: {
          id: 'fb',
          group: 'trading',
          type: 'line',
          height: 300
        },
        colors: ['#008FFB'],
        	tooltip: {
			custom: function({ series, seriesIndex, dataPointIndex, w }) {
				return '<div class="arrow_box">' +
					'<span>' + series[seriesIndex][dataPointIndex] + '</span>' +
					'</div>'
			},
			marker: {
				show: true,
			},
		},
			markers: {
			size: [3, 5, 5]
		},
		stroke: {
			curve: 'straight',
			width: 2,
		},
			xaxis: {
			type: 'datetime',
			labels: {
				//   rotate: -45,
				//   rotateAlways: true,
				formatter: function(value, timestamp, opts) {
					return timestampToDate(timestamp)
				}
			}
		},
        };

        var chart = new ApexCharts(document.querySelector("#chart-line"), options);
        chart.render();
      
      
}