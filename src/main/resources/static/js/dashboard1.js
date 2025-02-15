var chart; 
$(function () {
    "use strict";


  const fromdate =  "2025-02-11 00:00:00"; //formatDate(combineDateAndTime($("#dateFrom").val(),fromDate));
  const todate = "2025-02-12 00:00:00"; // formatDate(combineDateAndTime($("#dateTo").val(),toDate));
  
  var ohlcBox = document.getElementById("ohlc-info");
  
  let dataParam = {
		"fromDate": fromdate,
		"toDate": todate,
		"cryptoCurrencyCode": "BTC",
  		"period": $(".btn-group .btn.active").text().trim()
		// "cryptoCurrencyCode": $("#currencyDropDown").val(),
	};
	$.ajax({
		type: "POST",
		contentType: "application/json; charset=utf-8",
		url: "/getCandleGraphData",
		data: JSON.stringify(dataParam),
		dataType: 'json',
		timeout: 600000,
		success: function(response) {
		console.log(response);
	      response.dataNormal.data.forEach(item => {
				item.y = JSON.parse(item.y);
			});


    // Extract last candle's OHLC values
    let lastCandleIndex = response.dataNormal.data.length - 1;
    let lastCandle = response.dataNormal.data[lastCandleIndex];

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
          data: response.dataNormal.data
        }],
          chart: {
			   toolbar: {
		        show: true,
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
        title: {
          align: 'left'
        },
        xaxis: {  
		  labels: {
			style: {
              fontSize: '12px',
              fontFamily: 'Helvetica, Arial, sans-serif',
              fontWeight: 400,
              cssClass: 'fill-white',
          	},
		  },
          type: 'datetime'
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
		   tooltip: { enabled: true } ,// Show price tooltip on Y-axis
       
        },
        tooltip: {
                enabled: false // Disable default tooltip
            }
        };

        chart = new ApexCharts(document.querySelector("#chart"), options);
        chart.render();
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

  const fromdate =  "2025-02-11 00:00:00"; //formatDate(combineDateAndTime($("#dateFrom").val(),fromDate));
  const todate = "2025-02-12 00:00:00"; // formatDate(combineDateAndTime($("#dateTo").val(),toDate));
  
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
        response.dataNormal.data.forEach(item => {
            item.y = JSON.parse(item.y);
        });

        // Initialize chart only once
        if (!chart) {
            chart = new ApexCharts(document.querySelector("#chart"), {
                series: [{
                    data: response.dataNormal.data
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
            chart.updateSeries([{ data: response.dataNormal.data }]);
               $("#loading-spinner").hide();
        }
        
    },
    error: function(e) {
        console.log("ERROR:", e);
    }
});
	    
}
