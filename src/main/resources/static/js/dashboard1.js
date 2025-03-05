let chart, chartLine;
let livepriceData = [];
let fromDate, toDate;

$(document).ready(() => {
  let now = new Date();
  let oneHourAgo = new Date(now.getTime() - (4 * 60 * 60 * 1000)); // Subtract 4 hours

  // Format Dates for Flatpickr (YYYY-MM-DD HH:mm:ss)
  fromDate = formatDate(oneHourAgo);
  toDate = formatDate(now);

  // Initialize Flatpickr with Default Values
  $("#fromDate").flatpickr({
    enableTime: true,
    dateFormat: "Y-m-d H:i:ss",
    defaultDate: fromDate,
    onChange: function(_, dateStr) {
      fromDate = dateStr;
      if (fromDate && toDate) updateChart();
    }
  });

  $("#toDate").flatpickr({
    enableTime: true,
    dateFormat: "Y-m-d H:i:ss",
    defaultDate: toDate,
    onChange: function(_, dateStr) {
      toDate = dateStr;
      if (fromDate && toDate) updateChart();
    }
  });
  
  updateChart();
});

// Fetch data using async/await
async function fetchData(url, dataParam) {
  try {
    const response = await $.ajax({
      type: "POST",
      contentType: "application/json; charset=utf-8",
      url: url,
      data: JSON.stringify(dataParam),
      dataType: 'json',
      timeout: 30000
    });
    return response;
  } catch (error) {
    console.error("Fetch Error:", error);
    return null;
  }
}

// Update chart based on current global fromDate/toDate and selected period
async function updateChart() {
  $("#loading-spinner").show();

  let period = $(".btn-group .btn.active").text().trim();
  let dataParam = {
    "fromDate": fromDate,
    "toDate": toDate,
    "cryptoCurrencyCode": "BTC",
    "period": period,
    "dataType": "NORMAL",
  };

  let candleDataResponse, liveDataResponse;
  if (period === "Price") {
    // Only fetch candle data when only candlestick (with volume) should be shown
    candleDataResponse = await fetchData("/getCandleGraphData", dataParam);
  } else {
    [candleDataResponse, liveDataResponse] = await Promise.all([
      fetchData("/getCandleGraphData", dataParam),
      fetchData("/getGraphData", dataParam)
    ]);
  }

  if (!candleDataResponse || (period !== "Price" && !liveDataResponse)) {
    $("#loading-spinner").hide();
    return;
  }

  let candleData = candleDataResponse.dataCandle.data.map(item => ({
    x: item.x, // Keep as-is (convert if needed)
    y: JSON.parse(item.y)
  }));

  let volumeData = candleDataResponse.dataVolume.data;
  let volumeColors = candleData.map(candle =>
    candle.y[3] >= candle.y[0] ? "#00E396" : "#FF4560"
  );

  if (period !== "Price") {
    livepriceData = liveDataResponse.dataNormal.data.map(item => ({
      x: Number(item.x) * 1000,
      y: parseFloat(item.y)
    }));
  } else {
    livepriceData = [];
  }

  $("#loading-spinner").hide();
  renderChart(candleData, livepriceData, volumeData, volumeColors);
}

// Render Chart (ApexCharts settings remain unchanged)
function renderChart(candleData, livepriceData, volumeData, volumeColors) {
  let chartOptions = {
    series: [
      { data: candleData },
      { data: livepriceData, type: 'line' }
    ],
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
          document.getElementById("date").textContent = w.config.series[0].data[dataPointIndex].x;
          document.getElementById("open").textContent = open;
          document.getElementById("high").textContent = high;
          document.getElementById("low").textContent = low;
          document.getElementById("close").textContent = close;
          $("#ohlc-info").removeClass("d-none");
        },
        dataPointMouseLeave: function(event, chartContext, config) {
          // Optional: hide the OHLC info box when not hovering
        }
      },
    },
    plotOptions: {
      candlestick: {
        colors: { upward: "#00E396", downward: "#FF4560" },
        wick: { useFillColor: true }
      }
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
      tooltip: { enabled: true },
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
        show: true,
        width: 1,
        position: "front",
        stroke: {
          color: "#ffffff",
          width: 1,
          dashArray: 3
        }
      },
      tooltip: { enabled: true },
    },
    tooltip: {
      enabled: true,
      custom: function({ series, seriesIndex, dataPointIndex, w }) {
        return '';
      },
      style: {
        fontSize: '0px'
      },
      marker: {
        show: false
      },
      y: {
        show: false
      },
      x: {
        show: true
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
    colors: ['#2E93fA', '#66DA26', '#546E7A', '#E91E63', '#FF9800'],
    stroke: {
      show: true,
      curve: 'straight',
      lineCap: 'butt',
      colors: undefined,
      width: 2,
      dashArray: 0,
    },
    legend: {
      show: false
    },
  };

  let volumeChartOptions = {
    series: [{ name: 'volume', type: 'bar', data: volumeData }],
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
    legend: {
      show: false
    },
  };

  if (!chart) {
    chart = new ApexCharts(document.querySelector("#main-chart"), chartOptions);
    chart.render();
  } else {
    if (livepriceData && livepriceData.length) {
      chart.updateSeries([{ data: candleData }, { data: livepriceData }]);
    } else {
      chart.updateSeries([{ data: candleData }]);
    }
  }

  if (!chartLine) {
    chartLine = new ApexCharts(document.querySelector("#chart-line"), volumeChartOptions);
    chartLine.render();
  } else {
    chartLine.updateSeries([{ data: volumeData }]);
  }
}

function changeTimeframe(timeframe) {
  $(".btn-group .btn").removeClass("active");
  event.target.classList.add("active");
  if (fromDate && toDate) updateChart();
}

function formatDate(date) {
  return date.getFullYear() + "-" +
    String(date.getMonth() + 1).padStart(2, '0') + "-" +
    String(date.getDate()).padStart(2, '0') + " " +
    String(date.getHours()).padStart(2, '0') + ":" +
    String(date.getMinutes()).padStart(2, '0') + ":00";
}
