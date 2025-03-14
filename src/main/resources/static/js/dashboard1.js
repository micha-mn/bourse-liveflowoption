var chart;
var chartLine;
var now = new Date();
var todayMidnight = new Date(now.getFullYear(), now.getMonth(), now.getDate()); // Today at midnight
var yesterdayMidnight = new Date(todayMidnight);

yesterdayMidnight.setDate(yesterdayMidnight.getDate() - 5); // Yesterday at midnight
todayMidnight.setDate(todayMidnight.getDate() + 1);

var fromdate = formatDate(yesterdayMidnight); // Start of yesterday
var todate = formatDate(todayMidnight);        // Start of today (end of yesterday's full interval)

var previousXMin = null;

var page = 0;
const size = 50;

var totalPages = Infinity;
let windowSize = 25;
	
var allData = []; // Store fetched data
var visibleData = []; // Store fetched data
var isFetching = false;
var selectedInterval =$(".btn-group .btn.active").text().trim();
var originalXAxisOptions = {
  labels: {
    show: false,
    style: {
      fontSize: '12px',
      fontFamily: 'Helvetica, Arial, sans-serif',
      fontWeight: 400,
      cssClass: 'fill-white'
    }
  },
  type: 'category',
  tickAmount: 5, 
  crosshairs: {
    show: true,
    width: 1,
    position: "front",
    stroke: {
      color: "#ffffff",
      width: 1,
      dashArray: 3
    }
  },
  axisTicks: { show: false },
  axisBorder: { show: true },
  tooltip: { enabled: false },
  stepSize :10,
};

// Y-axis configuration
var originalYAxisOptions = {
  labels: {
    style: {
      fontSize: '12px',
      fontFamily: 'Helvetica, Arial, sans-serif',
      fontWeight: 400,
      cssClass: 'fill-white'
    }
  },
  crosshairs: {
    show: true,
    width: 1,
    position: "front",
    stroke: {
      color: "#ffffff",
      width: 1,
      dashArray: 3
    }
  },
  tooltip: { enabled: true }
};

// Chart configuration (general settings, events, toolbar, etc.)
var originalChartSettings = {
	animations: {
        enabled: false,
        },
  id: 'chart2',
  group: 'candle',
  zoom: {
    enabled: true,
    type: 'x'
  },
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
      reset: true || '<img src="/static/icons/reset.png" width="20">',
      customIcons: []
    }
  },
  type: 'candlestick',
  height: 350,
  events: {
  scrolled: function(chartContext, { xaxis }) {
    // Existing panning logic
   if (previousXMin === null) {
      previousXMin = xaxis.min;
      return;
    }
    if (xaxis.min < previousXMin && !isFetching) 
   	{	$("#loading-spinner").show();
		fetchMoreCandlestickData(xaxis);
	    if (previousXMin !== null) {
	      if (xaxis.min < previousXMin) {
	        console.log("Panned left (scroll left)");
	      } else if (xaxis.min > previousXMin) {
	        console.log("Panned right (scroll right)");
	      }
	    }
	    previousXMin = xaxis.min; 
    }
    // In category mode, xaxis.min and xaxis.max are indices.
    let visibleStartIndex = Math.floor(xaxis.min);
    let visibleEndIndex = Math.ceil(xaxis.max);
    
    // Clamp indices to the data array boundaries
    visibleStartIndex = Math.max(0, visibleStartIndex);
    visibleEndIndex = Math.min(allData.length - 1, visibleEndIndex);
    
    // Slice the allData array to get the visible subset.
    let visibleSubset = allData.slice(visibleStartIndex, visibleEndIndex + 1);
    
    // Calculate new y-axis limits (using 5% margin, for example)
    let newYLimits = calculateYAxisRangeFromVisibleData(visibleSubset, 5);
    	originalYAxisOptions.min = newYLimits.min;
		originalYAxisOptions.max = newYLimits.max;
    // Update the chart's y-axis options
    chart.updateOptions({
      yaxis:originalYAxisOptions
    });
  },
   beforeZoom: function(chartContext, { xaxis }) {
	    // In category mode, xaxis.min and xaxis.max are indices.
	    let visibleStartIndex = Math.floor(xaxis.min);
	    let visibleEndIndex = Math.ceil(xaxis.max);
	    
	    // Clamp indices to the data array boundaries
	    visibleStartIndex = Math.max(0, visibleStartIndex);
	    visibleEndIndex = Math.min(allData.length - 1, visibleEndIndex);
	    
	    // Slice the allData array to get the visible subset.
	    let visibleSubset = allData.slice(visibleStartIndex, visibleEndIndex + 1);
	    
	    // Calculate new y-axis limits (using 5% margin, for example)
	    let newYLimits = calculateYAxisRangeFromVisibleData(visibleSubset, 5);
	    	originalYAxisOptions.min = newYLimits.min;
			originalYAxisOptions.max = newYLimits.max;
	    // Update the chart's y-axis options
	    chart.updateOptions({
	      yaxis:originalYAxisOptions
	    });
  },
  zoomed: function(chartContext, { xaxis }) {
      	windowSize = xaxis.max - xaxis.min + 1;
      },
  dataPointMouseEnter: function(event, chartContext, { seriesIndex, dataPointIndex, w }) {
    let open = w.globals.seriesCandleO[seriesIndex][dataPointIndex];
    let high = w.globals.seriesCandleH[seriesIndex][dataPointIndex];
    let low = w.globals.seriesCandleL[seriesIndex][dataPointIndex];
    let close = w.globals.seriesCandleC[seriesIndex][dataPointIndex];
    document.getElementById("date").textContent = w.config.series[0].data[dataPointIndex].x;
    document.getElementById("open").textContent = open;
    document.getElementById("high").textContent = high;
    document.getElementById("low").textContent = low;
    document.getElementById("close").textContent = close;
    $("#ohlc-info").removeClass("d-none");
  }
}
};

// Plot options configuration
var originalPlotOptions = {
  candlestick: {
    colors: {
      upward: "#00E396",
      downward: "#FF4560"
    },
    wick: {
      useFillColor: true
    }
  }
};

// Tooltip configuration
var originalTooltipOptions = {
  enabled: true,
  custom: function({ series, seriesIndex, dataPointIndex, w }) {
    return '';
  },
  style: { fontSize: '0px' },
  marker: { show: false },
  y: { show: false },
  x: { show: false }
};

// Grid configuration
var originalGridOptions = {
  show: true,
  borderColor: '#3d4258',
  strokeDashArray: 0,
  position: 'back',
  xaxis: { lines: { show: true } },
  yaxis: { lines: { show: true } }
};


// Separate Chart Settings
var originalChartLineSettings = {
  id: 'chart1',
  height: 130,
  type: 'bar',
  group: 'candle',
  toolbar: { show: false }
};

// Separate Plot Options
var originalPlotOptionsLine = {
  bar: {
    distributed: true
  }
};

// Separate X-axis Options
var originalXAxisLineOptions = {
  type: 'category',
  tooltip: { enabled: false },
  labels: {
    style: {
      fontSize: '12px',
      fontFamily: 'Helvetica, Arial, sans-serif',
      fontWeight: 400,
      cssClass: 'fill-white'
    }
  }
};

// Separate Y-axis Options
var originalYAxisLineOptions = {
  labels: {
    show: false,
    formatter: function(value) {
      return value.toFixed(2);
    },
    style: {
      fontSize: '12px',
      fontFamily: 'Helvetica, Arial, sans-serif',
      fontWeight: 400,
      cssClass: 'fill-white'
    }
  }
};

// Separate Data Labels Options
var originalDataLabelsLineOptions = {
  enabled: false
};

// Separate Tooltip Options
var originalTooltipLineOptions = {
  enabled: true,
  shared: true,          // Ensures tooltips appear together on both charts
  intersect: false,      // Ensures hover aligns on the same date
  custom: function({ series, seriesIndex, dataPointIndex, w }) {
    return '';         // Hide tooltip content
  },
  style: {
    fontSize: '0px'    // Hide tooltip text
  },
  marker: { show: false },   // No tooltip markers
  y: { show: false },        // No Y-axis tooltip
  x: { show: false }         // No X-axis tooltip
};

// Separate Legend Options
var originalLegendLineOptions = {
  show: false
};

// Assemble the full chart options
var options = {
  chart: originalChartSettings,
  plotOptions: originalPlotOptions,
  title: { align: 'left' },
  xaxis: originalXAxisOptions,
  yaxis: originalYAxisOptions,
  tooltip: originalTooltipOptions,
  grid: originalGridOptions
};
// Assemble the full optionsLine object
var optionsLine = {
  chart: originalChartLineSettings,
  plotOptions: originalPlotOptionsLine,
  xaxis: originalXAxisLineOptions,
  yaxis: originalYAxisLineOptions,
  dataLabels: originalDataLabelsLineOptions,
  tooltip: originalTooltipLineOptions,
  legend: originalLegendLineOptions
};
     
$(document).ready(() => {


	var ohlcBox = document.getElementById("ohlc-info");

	let dataParam = {
		"fromDate": fromdate,
		"toDate": todate,
		"cryptoCurrencyCode": "BTC",
		"period": selectedInterval,
		 page: page,
         size: size
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
			
			totalPages = response.dataCandle.totalPages; // Update total pages
            page++; // Increase page number
			console.log(response);

			response.dataCandle.data.forEach(item => {
				item.y = JSON.parse(item.y);
			});

 			allData = response.dataCandle.data.concat(allData);
			let data2 = response.dataVolume.data;
			let volumeData = [];
			let volumeColors = []; // ✅ Array to hold bar colors
			let totalDataPoints =  response.dataCandle.data.length;
    		let min = totalDataPoints - windowSize <0 ? 0 :totalDataPoints - windowSize;
    		let max = totalDataPoints
			originalXAxisOptions.min=min;
			originalXAxisOptions.max=max;
			
  		   let newYLimits = calculateYLimitsCandleSticks(response.dataCandle.data, min, max,10);
      		originalYAxisOptions.min = newYLimits.min;
			originalYAxisOptions.max = newYLimits.max;
			

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
			
			options.series = [{
				data: response.dataCandle.data
			}];
			chart = new ApexCharts(document.querySelector("#main-chart"), options);
			chart.render();

			optionsLine.series = [{
				name: 'volume',
				type: 'bar',
				data: data2
			}];

			optionsLine.colors = volumeColors;

			chartLine = new ApexCharts(document.querySelector("#chart-line"), optionsLine);
			// chartLine.render();


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
	 fromdate = formatDate(yesterdayMidnight); // Start of yesterday
     todate = formatDate(todayMidnight);        // Start of today (end of yesterday's full interval)

	 page = 0;
	
	 totalPages = Infinity;
	 windowSize = 50;
	 selectedInterval =$(".btn-group .btn.active").text().trim()	;
	 allData = []; // Store fetched data
	 
	var ohlcBox = document.getElementById("ohlc-info");
	
	let dataParam = {
		"fromDate": fromdate,
		"toDate": todate,
		"cryptoCurrencyCode": "BTC",
		"period": timeframe,
		 page: page, 
         size: size
		// "cryptoCurrencyCode": $("#currencyDropDown").val(),
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
			
			
			totalPages = response.dataCandle.totalPages; // Update total pages
            page++; // Increase page number
			console.log(response);

			response.dataCandle.data.forEach(item => {
				item.y = JSON.parse(item.y);
			});

 			allData = response.dataCandle.data.concat(allData);
			let data2 = response.dataVolume.data;
			let volumeData = [];
			let volumeColors = []; // ✅ Array to hold bar colors
			let totalDataPoints =  response.dataCandle.data.length;
    		let min = totalDataPoints - windowSize  <0 ? 0 : totalDataPoints - windowSize;
    		let max = totalDataPoints
			originalXAxisOptions.min=min;
			originalXAxisOptions.max=max;
			
  		   let newYLimits = calculateYLimitsCandleSticks(response.dataCandle.data, min, max,10);
      		originalYAxisOptions.min = newYLimits.min;
			originalYAxisOptions.max = newYLimits.max;
			

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
			
			options.series = [{
				data: response.dataCandle.data
			}];
			if (!chart) {
			chart = new ApexCharts(document.querySelector("#main-chart"), options);
			chart.render();
				optionsLine.series = [{
				name: 'volume',
				type: 'bar',
				data: data2
			}];

			optionsLine.colors = volumeColors;

			chartLine = new ApexCharts(document.querySelector("#chart-line"), optionsLine);
			}
			else {
				let totalDataPoints =  response.dataCandle.data.length;
	    		let min = totalDataPoints - windowSize < 0 ? 0 : totalDataPoints - windowSize;
	    		let max = totalDataPoints;
				originalXAxisOptions.min=min;
			    originalXAxisOptions.max=max;
				
				
	  		   let newYLimits = calculateYLimitsCandleSticks(response.dataCandle.data, min, max,10);
	      		
				originalYAxisOptions.min = newYLimits.min;
			    originalYAxisOptions.max = newYLimits.max;
			    
				 chart.updateOptions({
				  series:[{
						data:allData
					}],
					 xaxis:originalXAxisOptions,
			     	 yaxis:originalYAxisOptions
			    });
				
				$("#loading-spinner").hide();
			}
			
		

		},
		error: function(e) {
			console.log("ERROR:", e);
		}
	});

}

function formatDate(date) {
	return date.getFullYear() + "-" +
		String(date.getMonth() + 1).padStart(2, '0') + "-" +
		String(date.getDate()).padStart(2, '0') + " " +
		String(date.getHours()).padStart(2, '0') + ":" +
		String(date.getMinutes()).padStart(2, '0') + ":00";
}

function fetchMoreCandlestickData(xaxis) {
  isFetching = true;
  
  // Update your fromDate based on the earliest data in allData
  if(totalPages==1)
  {
	  let earliestfromdate = new Date(fromdate);
	  let earliesttoadate = new Date(fromdate);
	   earliestfromdate.setDate(earliestfromdate.getDate() - 1);
	  fromdate = formatDate(earliestfromdate);
	  todate = formatDate(earliesttoadate);
	  page=0;
  }
  let dataParam = {
    "fromDate": fromdate,
    "toDate": todate, // your global toDate variable
    "cryptoCurrencyCode": "BTC",
    "period": selectedInterval,
    page: page,
    size: size
  };

  $.ajax({
    type: "POST",
    contentType: "application/json; charset=utf-8",
    url: "/getCandleGraphData",
    data: JSON.stringify(dataParam),
    dataType: 'json',
    timeout: 600000,
    success: function(response) {
		console.log(fromdate,todate);
		totalPages = response.dataCandle.totalPages; // Update total pages
        page++; // Increase page number
	    console.log(response);
			
			$("#loading-spinner").hide();
    	 	 // Process response and prepend to your allData array
      		response.dataCandle.data.forEach(item => {
				item.y = JSON.parse(item.y);
			});
			
            let oldData=allData.length;
 			allData = response.dataCandle.data.concat(allData);
			let data2 = response.dataVolume.data;
			let volumeData = [];
			let volumeColors = []; // ✅ Array to hold bar colors
			let totalDataPoints =  allData.length;
			let dynamicRange= totalDataPoints - oldData;
    		let min = dynamicRange - windowSize <0 ? 0 : dynamicRange - windowSize ;
    		let max = dynamicRange;
			originalXAxisOptions.min=min;
			originalXAxisOptions.max=max;
			
  		   let newYLimits = calculateYLimitsCandleSticks(allData, min, max,10);
      		originalYAxisOptions.min = newYLimits.min;
			originalYAxisOptions.max = newYLimits.max;
			

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

			
      chart.updateOptions({
		  series:[{
				data:allData
			}],
			 xaxis:originalXAxisOptions,
	     	 yaxis:originalYAxisOptions
	    });
	    
      page++;
      isFetching = false;
    },
    error: function(e) {
      console.log("Fetch error:", e);
      isFetching = false;
    }
  });
}
function calculateYLimitsCandleSticks(data, xMin, xMax, marginPercentage) {
 	   const visibleDataArray = data.slice(xMin, xMax);
       const yValues = visibleDataArray.reduce((acc, point) => {
	    if (Array.isArray(point.y)) {
	      return acc.concat(point.y);
	    } else {
	      acc.push(point.y);
	      return acc;
	    }
	  }, []);
      let yMin = Math.min(...yValues);
      let yMax = Math.max(...yValues);
      // Add a little padding
      const margin = (yMin - yMax) * (marginPercentage / 100);

      return { min: yMin -  Math.abs(margin), max: yMax +  Math.abs(margin) };
    }
    
  function calculateYLimits(data, xMin, xMax, padding = 5) {
      const visibleData = data.filter(point => point.id >= xMin && point.id <= xMax);
      const yValues = visibleData.map(point => point.y);
      let yMin = Math.min(...yValues);
      let yMax = Math.max(...yValues);
      // Add a little padding
      return { min: yMin - padding, max: yMax + padding };
    }
    
    function calculateYAxisRangeFromVisibleData(data, marginPercentage) {
	  // Gather all y values from the visible data
	  const yValues = data.reduce((acc, point) => {
	    if (Array.isArray(point.y)) {
	      return acc.concat(point.y);
	    } else {
	      acc.push(point.y);
	      return acc;
	    }
	  }, []);
	  let yMin = Math.min(...yValues);
	  let yMax = Math.max(...yValues);
	  // Calculate a margin based on the difference
	  const margin = (yMax - yMin) * (marginPercentage / 100);
	  return { min: yMin - margin, max: yMax + margin };
	}

    