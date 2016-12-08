/* 2016 
 * Tommy Dang (on the Scagnostics project, as Assistant professor, iDVL@TTU)
 *
 * THIS SOFTWARE IS BEING PROVIDED "AS IS", WITHOUT ANY EXPRESS OR IMPLIED
 * WARRANTY.  IN PARTICULAR, THE AUTHORS MAKE NO REPRESENTATION OR WARRANTY OF ANY KIND CONCERNING THE MERCHANTABILITY
 * OF THIS SOFTWARE OR ITS FITNESS FOR ANY PARTICULAR PURPOSE.
 */


var selectedScag = "Outlying";
var color10 = d3.scale.category10();

 // *************************BRUSHING **********************************
 var brushCell;
// Clear the previously-active brush, if any.
function brushstart(p) {
  if (brushCell !== this) {
    d3.select(brushCell).call(brush.clear());
    x.domain(domainByTrait);
    y.domain(domainByTrait);
    brushCell = this;
  }
}

// Highlight the selected circles.
function brushmove(p) {
  var e = brush.extent();
  svg.selectAll("circle").classed("hidden", function(d) {
    return e[0][0] > d[p.x] || d[p.x] > e[1][0]
        || e[0][1] > d[p.y] || d[p.y] > e[1][1];
  });
}

// If the brush is empty, select all circles.
function brushend() {
  if (brush.empty()) svg.selectAll(".hidden").classed("hidden", false);
}

function getIndex(mi, mj){
  if (mi<mj){
    return mj*(mj-1)/2+mi; 
  }
  else if (mi>mj){
    return mi*(mi-1)/2+mj; 
  }
}    


 // Find the most different
 function findMostDifferent(){
  var n = traits.length;
  var pairList = [];
  var varList = [];
  for (i = 0; i < n; i++){ 
    for (j = i+1; j < n; j++) {
      var pair1 = getIndex(i,j);
      for (k = 0; k < n; k++) {
        if (k==i || k==j) continue;
        var pair2 = getIndex(k,i);
        var pair3 = getIndex(k,j);
        if (dataS[pair1]["Monotonic"]>0.8){
          var dif = Math.abs(dataS[pair2][selectedScag]-dataS[pair3][selectedScag]);
          if (dif>0.5){
            varList.push(i);
            varList.push(j);
            varList.push(k);  

            console.log(traits[i]+" "+traits[j]+" "+traits[k]);
            pairList.push({x: traits[i], i: varList.length-3, y: traits[j], j: varList.length-2,
           mi: i, mj: j});
            pairList.push({x: traits[i], i: varList.length-3, y: traits[k], j: varList.length-1,
           mi: i, mj: k});
            pairList.push({x: traits[j], i: varList.length-2, y: traits[k], j: varList.length-1,
           mi: j, mj: k});
                     
          }
        }
      }  
    }  
  }   
    var cell = svg.selectAll(".cell")
      .data(pairList).enter()
      .append("g")
        .attr("class", "cell")
        .attr("transform", function(d) { return "translate(" + (d.i%3 * size) + "," + ((d.j-0.8)*size) + ")"; })
        .each(plot);
    
    svg.selectAll(".varText2") 
      .data(varList).enter()
      .append("text")
        .style("font-size", "20px")
        .attr("class", "varText2")
        .attr("x", function(d,i){ return i%3 * size+25;  })
        .attr("y", function(d,i){ return (i+0.12) * size; })
        .text(function(d,i) { return traits[d]; });
}



// Plot function *******************************
function plot(p) {
  //if ((p.i>p.j|| p.i==p.j))
  //  return;
  var cell = d3.select(this);
  
  cell.append("rect")
      .attr("class", "frame")
      .attr("x", padding / 2)
      .attr("y", padding / 2)
      .attr("width", size - padding)
      .attr("height", size - padding)
      .style("fill", function(d) { 
        return "#ddd";
          if (p.mi<p.mj){
             var k = p.mj*(p.mj-1)/2+p.mi; 
             return colorRedBlue(dataS[k][selectedScag]);
          }
          else if (p.mi>p.mj){
            var k = p.mi*(p.mi-1)/2+p.mj; 
            return colorRedBlue(dataS[k][selectedScag]);
          }
          else{
            return "#000";
          }
      })
      .style("stroke-width", function(d){
        if (p.leaderi)
          return Math.sqrt(p.leaderi.children.length + p.leaderj.children.length)+1;
        else
          return 1;
      });   
  cell.selectAll("circle")
      .data(data)
    .enter().append("circle")
      .attr("cx", function(d) { return x(d[p.x]); })
      .attr("cy", function(d) { return y(d[p.y]); })
      .attr("r", size/50)
      .style("fill", "#000"); 

  // Show score on each plot    
  cell.append("text")
      .attr("class", "scoreCellText")
      .attr("x", 3)
      .attr("y", 14)
      .attr("font-family", "sans-serif")
      .attr("font-size", "8px")
      .style("text-shadow", "1px 1px 0 rgba(0, 0, 0, 0.7")
      .style("fill", "#ff0")
      .text(function(d,i) { 
        var k = -1;  
        if (p.mi<p.mj){
          k = p.mj*(p.mj-1)/2+p.mi; 
        }
        else if (p.mi>p.mj){
           k = p.mi*(p.mi-1)/2+p.mj; 
        }
        return parseFloat(dataS[k][selectedScag]).toFixed(2); })
      .style("fill-opacity", function(){
        return document.getElementById("checkbox1").checked ? 1 : 0;
      });         
} // End plot functiong

function drawScagHistogram(pairId, x_, y_, w, h){
  var a=[];
  for (var key in dataS[pairId]){
    var obj={};
    obj.name=key;
    obj.value=dataS[pairId][key];
    a.push(obj);
  }
  var x = d3.scale.ordinal().rangeRoundBands([0, w], .05);
      y = d3.scale.linear().rangeRound([h, 0]);

var xAxis = d3.svg.axis()
    .scale(x)
    .orient("bottom");

var yAxis = d3.svg.axis()
    .scale(y)
    .orient("left")
    .innerTickSize(-w)
    .ticks(5);

  var g = svg.append("g")
    .attr("transform", "translate(" + x_+ "," + y_+ ")");

  
  x.domain(a.map(function(d) { return d.name; }));
  y.domain([0, 1]);

  g.append("g")
      .attr("class", "x axis")
      .attr("transform", "translate(0," + h + ")")
      .call(xAxis)
    .selectAll("text")
      .style("text-anchor", "end")
      .attr("dx", "-1.5em")
      .attr("dy", "-.55em")
      .style("fill", color10)
      .style("font-size", "14px")
      .attr("transform", "translate(25,0)rotate(-30)" );

  g.append("g")
      .attr("class", "y axis")
      .call(yAxis);

  g.selectAll("bar")
      .data(a)
    .enter().append("rect")
      .style("fill", function(d){ return color10(d.name);})
      .attr("x", function(d,i) { return x(d.name); })
      .attr("width", x.rangeBand())
      .attr("y", function(d) { return y(d.value); })
      .attr("height", function(d) { return h - y(d.value); });
}

