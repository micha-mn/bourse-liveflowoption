  var chart;
  $( document ).ready(function() {
	   	    $("#dateFrom").jqxDateTimeInput({  formatString: "F", showTimeButton: true, width: '325px', height: '40px' });
	   	    $("#dateTo").jqxDateTimeInput({  formatString: "F", showTimeButton: true, width: '325px', height: '40px' });

	   
			
			
	});
	function drawGraph(){
		
		
		  dataParam = { 
	        		"fromDate":formatDate($("#dateFrom").val()),
	        	    "toDate":formatDate($("#dateTo").val()),
	        	    "dataType":'normal',
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

        var options = {
          series: [{
            name: "ENNA",
            data: jsonArrayWithNumberTimestamp
        },{
            name: "MIN",
            data: jsonArrayWithNumberTimestamp1
        },
        {
            name: "MAX",
            data: jsonArrayWithNumberTimestamp2
        }],
          chart: {
          height: 350,
          type: 'line',
	         toolbar: {
	        show: true,
	        }
        },
        dataLabels: {
          enabled: false
        },
        stroke: {
          curve: 'straight',
          width: 2,
        },
        title: {
          text: '',
          align: 'left'
        },
        grid: {
         /* row: {
            colors: ['#f3f3f3', 'transparent'], // takes an array which will be repeated on columns
            opacity: 0.5
          },*/
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
		     				    		     minWidth: 75,maxWidth: 75,
			 				        		
							},
	     				          tickAmount: 6,
	     				    	 min: Math.sign(min) == -1 ? -Math.abs(min) - values : Math.abs(min) - values,
								 max: Math.sign(max) == -1 ? -Math.abs(max) + values : Math.abs(max) + values,		
		    			 		 axisBorder: {
	     					                  width: 3,
	     					                  show: true,
	     					                  color: '#ffffff',
	     					                  offsetX: 0,
	     					                  offsetY: 0
	     					              },
	     				    	 }
	     				    	
        };
if (chart != null)
		{
			chart.updateOptions({
          series:  [{
            name: "ENNA",
            data: jsonArrayWithNumberTimestamp
        },{
            name: "MAX",
            data: jsonArrayWithNumberTimestamp1
        },
        {
            name: "MIN",
            data: jsonArrayWithNumberTimestamp2
        }],
          chart: {
          height: 350,
          type: 'line',
	         toolbar: {
	        show: true,
	        }
        },
        dataLabels: {
          enabled: false
        },
        stroke: {
          curve: 'straight',
          width: 2,
        },
        title: {
          text: '',
          align: 'left'
        },
        grid: {
         /* row: {
            colors: ['#f3f3f3', 'transparent'], // takes an array which will be repeated on columns
            opacity: 0.5
          },*/
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
		     				    		     minWidth: 75,maxWidth: 75,
			 				        		
							},
	     				          tickAmount: 6,
	     				    	 min: Math.sign(min) == -1 ? -Math.abs(min) - values : Math.abs(min) - values,
								 max: Math.sign(max) == -1 ? -Math.abs(max) + values : Math.abs(max) + values,		
		    			 		 axisBorder: {
	     					                  width: 3,
	     					                  show: true,
	     					                  color: '#ffffff',
	     					                  offsetX: 0,
	     					                  offsetY: 0
	     					              },
	     				    	 }
	     				    	
        });
		}
		else {
			 chart = new ApexCharts(document.querySelector("#chart"), options);
        	 chart.render();
		}
		
       
      
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