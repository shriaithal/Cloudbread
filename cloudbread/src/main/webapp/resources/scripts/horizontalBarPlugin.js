//Create horizontalBar plug-in for ChartJS
  var originalLineDraw = Chart.controllers.horizontalBar.prototype.draw;
  Chart.helpers.extend(Chart.controllers.horizontalBar.prototype, {
  
      draw: function () {
          originalLineDraw.apply(this, arguments);
  
          var chart = this.chart;
          var ctx = chart.chart.ctx;
  
          var index = chart.config.options.lineAtIndex;
          if (index) {
  
              var xaxis = chart.scales['x-axis-0'];
              var yaxis = chart.scales['y-axis-0'];
  
              var x1 = xaxis.getPixelForValue(index);                       
              var y1 = 0;                                                   
  
              var x2 = xaxis.getPixelForValue(index);                       
              var y2 = yaxis.height;                                        
  
              ctx.save();
              ctx.beginPath();
              ctx.moveTo(x1, y1);
              ctx.strokeStyle = 'red';
              ctx.lineTo(x2, y2);
              ctx.stroke();
  
              ctx.restore();
          }
      }
  });
