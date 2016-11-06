/* 2016 
 * Tommy Dang (on the Scagnostics project, as Assistant professor, iDVL@TTU)
 *
 * THIS SOFTWARE IS BEING PROVIDED "AS IS", WITHOUT ANY EXPRESS OR IMPLIED
 * WARRANTY.  IN PARTICULAR, THE AUTHORS MAKE NO REPRESENTATION OR WARRANTY OF ANY KIND CONCERNING THE MERCHANTABILITY
 * OF THIS SOFTWARE OR ITS FITNESS FOR ANY PARTICULAR PURPOSE.
 */

var tipWidth = 350;
var tipSVGheight = 350;
var tip_svg;
var y_svg;

var colorHighlight = "#fc8";
var buttonColor = "#ddd";
var cellHeight = 14;

var tip = d3.tip()
  .attr('class', 'd3-tip')
  .offset([-(tipSVGheight),0])
  .style('border', '1px solid #555');

function showTip(d) { 
  tip.html(function(d) {
    var str ="";
    return str; 
  });

  tip.show(d); 
  /*tip_svg.append('text')
    .attr("x", 0)
    .attr("y", 20)
    .style("font-family", "sans-serif")
    .style("font-size", "12px")
    .style("font-weight", "bold")
    .style("text-anchor", "start")
    .text("Cluster view")
    .style("fill", "#000");
  */  
  if (d.children) {  // diagonal varable names in the second matrix
    var pairList = cross2(d);
    var varList = [d.mi]; 
    d.children.forEach(function (d2){
      varList.push(d2);
    });

    tip.offset([-(varList.length-1)*size-20,20])
    tip_svg = d3.select('.d3-tip').append('svg')
      .attr("width", varList.length*size)
      .attr("height", (varList.length-1)*size);
    
    splom2(tip_svg, pairList, varList);  
  }
  else{ // cells in the second matrix
    var pairList = cross3(d);
    var varList1 = [d.mi];
    d.leaderi.children.forEach(function (d2){
      varList1.push(d2);
    });
    var varList2 = [d.mj];
    d.leaderj.children.forEach(function (d2){
      varList2.push(d2);
    });
    
    tip.offset([-(varList2.length)*size-20,20])
    tip_svg = d3.select('.d3-tip').append('svg')
      .attr("width", (varList1.length+1)*size)
      .attr("height", (varList2.length)*size);
    
    splom3(tip_svg, pairList, varList1, varList2);  
  }
  
}    


function cross2(d) {
  var c = [];
  var n = d.children.length;
    for (var i = -1; i < n; i++) 
    for (var j = i+1; j < n; j++) {
      var mi = i<0 ? d.mi : d.children[i];
      var mj = j<0 ? d.mi : d.children[j];

      c.push({x: traits[mi], i: (i+1), y: traits[mj], j: (j+1),
      mi: mi, mj: mj});
  }
  return c;
 
}

function cross3(d) {
  var c = [];
  if (d.leaderi== undefined || d.leaderi.children == undefined)
    return c;
  else{
    var n = d.leaderi.children.length;
    var m = d.leaderj.children.length;
    for (var i = -1; i < n; i++) 
      for (var j = -1; j < m; j++) {
        var mi = i<0 ? d.mi : d.leaderi.children[i];
        var mj = j<0 ? d.mj : d.leaderj.children[j];

        c.push({x: traits[mi], i: (i+1), y: traits[mj], j: (j+1),
        mi: mi, mj: mj});
    }
    return c;
  }
}

// splom function ****************************
function splomMain(svg_, pairList, varList) {
  var cell = svg_.selectAll(".cell")
    .data(pairList).enter()
    .append("g")
      .attr("class", "cell")
      .attr("transform", function(d) { return "translate(" + (d.i) * size + "," + d.j * size + ")"; })
      .each(plot)
      .on('mouseover', function(d) {
        showTip(d); 
      }); 
  // Titles for the diagonal.
  svg_.selectAll(".varText")
    .data(varList).enter()
    .append("text")
      .attr("class", "varText")
      .attr("x", function(d,i){ return i * size; })
      .attr("y", function(d,i){ return (i+0.8) * size; })
      .text(function(d,i) { return traits[d.mi] + " ("+(d.children.length+1)+")"; })
      .on('mouseover', function(d) {showTip(d); })
      .on('mouseout', function(d) {
        //tip.hide(d); 
      });
    // Brushing    
    //  cell.call(brush); 
}  

function splom2(svg_, pairList, varList) {
  var cell = svg_.selectAll(".cell")
    .data(pairList).enter()
    .append("g")
      .attr("class", "cell")
      .attr("transform", function(d) { return "translate(" + d.i * size + "," + ((d.j-1) * size+3) + ")"; })
      .each(plot);
  
  svg_.selectAll(".varText") 
    .data(varList).enter()
    .append("text")
      .attr("class", "varText")
      .attr("x", function(d,i){ return i * size;  })
      .attr("y", function(d,i){ return (i) * size; })
      .text(function(d,i) { return traits[d]; });
}  

function splom3(svg_, pairList, varList1, varList2) {
  var cell = svg_.selectAll(".cell")
    .data(pairList).enter()
    .append("g")
      .attr("class", "cell")
      .attr("transform", function(d) { return "translate(" + (d.i+1) * size + "," + (d.j * size +10) + ")"; })
      .each(plot); 

  svg_.selectAll(".varText") 
    .data(varList1).enter()
    .append("g").attr("transform", function(d,i) {
      return "translate(" +(i+1) * size + "," + 10 + ")"+ " rotate(-10)" 
    })
    .append("text")
      .attr("class", "varText3")
      .attr("x", 0)
      .attr("y", 0)
      .text(function(d,i) { return traits[d]; });
  svg_.selectAll(".varText4") 
    .data(varList2).enter()
    .append("g").attr("transform", function(d,i) {
      return "translate(" +(size-2)+ "," + (i+1)*size + ")"
    })
    .append("text")
      .style("text-anchor", "end")
      .attr("class", "varText")
      .attr("x", 0)
      .attr("y", 0)
      .text(function(d,i) { return traits[d]; });       
}  

// Plot function *******************************
function plot(p) {
  //if ((p.i>p.j|| p.i==p.j))
  //  return;
  var cell = d3.select(this);
  x.domain(domainByTrait);
  y.domain(domainByTrait);

  cell.append("rect")
      .attr("class", "frame")
      .attr("x", padding / 2)
      .attr("y", padding / 2)
      .attr("width", size - padding)
      .attr("height", size - padding)
      .style("fill", function(d) { 
          if (p.mi<p.mj){
             var k = p.mj*(p.mj-1)/2+p.mi; 
             return colorRedBlue(dataS[k]["Monotonic"]);
          }
          else if (p.mi>p.mj){
            var k = p.mi*(p.mi-1)/2+p.mj; 
            return colorRedBlue(dataS[k]["Monotonic"]);
          }
          else{
            return "#000";
          }
      });
  cell.append("text")
      .attr("class", "varTextCell")
      .attr("x", 0)
      .attr("y", 20)
      .text(function(d,i) { 
        var k = p.mj*(p.mj-1)/2+p.mi; 
            
      return parseFloat(dataS[k]["Monotonic"]).toFixed(2); });       
  
    
  cell.selectAll("circle")
      .data(data)
    .enter().append("circle")
      .attr("cx", function(d) { return x(d[p.x]); })
      .attr("cy", function(d) { return y(d[p.y]); })
      .attr("r", size/30)
      .style("fill", "#000");  
} // End plot functiong
