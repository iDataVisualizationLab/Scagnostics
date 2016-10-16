/* 2016 
 * Tommy Dang (on the Scagnostics project, as Assistant professor, iDVL@TTU)
 *
 * THIS SOFTWARE IS BEING PROVIDED "AS IS", WITHOUT ANY EXPRESS OR IMPLIED
 * WARRANTY.  IN PARTICULAR, THE AUTHORS MAKE NO REPRESENTATION OR WARRANTY OF ANY KIND CONCERNING THE MERCHANTABILITY
 * OF THIS SOFTWARE OR ITS FITNESS FOR ANY PARTICULAR PURPOSE.
 */

var tipWidth = 470;
var tipSVGheight = 370;
var tip_svg;
var y_svg;

var colorHighlight = "#fc8";
var buttonColor = "#ddd";
var cellHeight = 14;

var tip = d3.tip()
  .attr('class', 'd3-tip')
  .offset([-(tipSVGheight),-tipWidth/2])
  .style('border', '1px solid #555');

function showTip(d) { 
  tip.html(function(d) {
    var str ="";
    return str; 
  });

  tip.show(d);
   
  tip_svg = d3.select('.d3-tip').append('svg')
    .attr("width", tipWidth)
    .attr("height", tipSVGheight);

  tip_svg.append('text')
    .attr("x", 0)
    .attr("y", 20)
    .style("font-family", "sans-serif")
    .style("font-size", "12px")
    .style("font-weight", "bold")
    .style("text-anchor", "start")
    .text("Protein data")
    .style("fill", "#000");

  var varList = cross2(d);
  splom(tip_svg, varList);  
}     

function cross2(d) {
  var c = [];
  if (d.leaderi== undefined || d.leaderi.children == undefined)
    return c;
  else{
    

    var n = d.leaderi.children.length;
    for (var i = -1; i < n; i++) 
      for (var j = -1; j < n; j++) {
        var mi = i<0 ? d.mi :d.leaderi.children[i];
        var mj = j<0 ? d.mi : d.leaderi.children[j];

        c.push({x: traits[mi], i: (i+1), y: traits[mj], j: (j+1),
        mi: mi, mj: mj});
    }
    return c;
  }
}

// splom function ****************************
function splom(svg_, varList) {
  var cell = svg_.selectAll(".cell")
    .data(varList)
    .enter().append("g")
    .attr("class", "cell")
    .attr("transform", function(d) { return "translate(" + (d.i) * size + "," + d.j * size + ")"; })
    .each(plot);

  // Titles for the diagonal.
  cell.filter(function(d) { return d.i === d.j; }).append("text")
      .attr("class", "varText")
      .attr("x", padding)
      .attr("y", size*0.8)
      .text(function(d,i) { return d.x; })
      .on('mouseover', function(d) {
        showTip(d); 
      })
      .on('mouseout', function(d) {
        //tip.hide(d); 
     //   removeTimeArcs();
      });
  // Brushing    
  //  cell.call(brush);   
}  

// Plot function *******************************
function plot(p) {
  if ((p.i>p.j|| p.i==p.j))
    return;
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
          if (p.i<p.j){
             var k = p.mj*(p.mj-1)/2+p.mi; 
             return colorRedBlue(dataS[k]["Monotonic"]);
          }
          else if (p.i>p.j){
            var k = p.mi*(p.mi-1)/2+p.mj; 
            return colorRedBlue(dataS[k]["Monotonic"]);
          }
          else{
            return "#fff";
          }
      });
  cell.selectAll("circle")
      .data(data)
    .enter().append("circle")
      .attr("cx", function(d) { return x(d[p.x]); })
      .attr("cy", function(d) { return y(d[p.y]); })
      .attr("r", size/30)
      .style("fill", "#000");  
} // End plot functiong
