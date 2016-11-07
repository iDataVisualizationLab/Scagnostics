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

// Define the line
var valueline = d3.svg.line()
    .x(function(d) { return d.x; })
    .y(function(d) { return d.y; })
    .interpolate("basis");

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

  if (document.getElementById("radio1").checked){
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
  else if (document.getElementById("radio2").checked){
    var plotSize = 300;
    tip.offset([0,100])
    tip_svg = d3.select('.d3-tip').append('svg')
      .attr("width", plotSize+40)
      .attr("height", plotSize+40);    

    linkedScatterplot(tip_svg, plotSize, d);
  }  
  else if (document.getElementById("radio3").checked){
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
      return "translate(" +(i+1) * size + "," + 10 + ")"+ " rotate(-15)" 
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
      .attr("r", size/30)
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
        return parseFloat(dataS[k]["Monotonic"]).toFixed(2); })
      .style("fill-opacity", function(){
        return document.getElementById("checkbox1").checked ? 1 : 0;
      });         
} // End plot functiong

function toggleScore() {
  if (document.getElementById("checkbox1").checked){
    svg.selectAll(".scoreCellText")
      .style("fill-opacity", 1);
  }
  else{
    svg.selectAll(".scoreCellText")
      .style("fill-opacity", 0);
  }      

}  

function linkedScatterplot(svg_, plotSize, d) {
  var padding = 40;  
  var x = d3.scale.linear()
    .range([padding+10, plotSize+padding-10]);
  var y = d3.scale.linear()
      .range([plotSize+padding-10, padding+10]);

  x.domain(domainByTrait);
  y.domain(domainByTrait);

  var pairList; 
  if (d.children) {  // diagonal varable names in the second matrix
    pairList = cross2(d);      
  }
  else{
    pairList = cross3(d);
  }
  if (pairList.length==0) return;  // No scatterplot (1 variable in the cluster)
  var p=-1;
  var sumS = 0;
  for (var i=0;i<pairList.length;i++){
    p=pairList[i];
    var k = -1;  
    if (p.mi<p.mj){
      k = p.mj*(p.mj-1)/2+p.mi; 
    }
    else if (p.mi>p.mj){
       k = p.mi*(p.mi-1)/2+p.mj; 
    }
    sumS +=+dataS[k]["Monotonic"];
  }
  //console.log("sumS="+(sumS/pairList.length));

  svg_.append("rect")
    .attr("class", "frame2")
    .attr("x",padding)
    .attr("y",padding)
    .attr("width", plotSize)
    .attr("height", plotSize)
    .style("stroke", "#000") 
    .style("fill", function(d) { 
      if (pairList.length>0)
        return colorRedBlue(sumS/pairList.length);
      else
        return "#fff";  
    });   


  svg_.selectAll(".line")
      .data(data)
    .enter().append("path")
        .attr("class", "line")
        .attr("d", function(d) {
          var a = [];
          for (var i=0;i<pairList.length;i++){
            p=pairList[i];
            var obj ={};
            obj.x = x(d[p.x]);
            obj.y = y(d[p.y]);
            a.push(obj);
          }  
          return valueline(a);
        }); 

  svg_.selectAll("circle")
      .data(data)
    .enter().append("circle")
      .attr("cx", function(d) { return x(d[p.x]); })
      .attr("cy", function(d) { return y(d[p.y]); })
      .attr("r", plotSize/80)
      .style("fill", "#000");   

  // Compute variable names ****************************************
  var varList1, varList2; 
  if (d.children) {  // diagonal varable names in the second matrix
    varList1 = [d.mi]; 
    d.children.forEach(function (d2){
      varList1.push(d2);
    });    
    varList2 = varList1; 
  }
  else{ // cells in the second matrix
     varList1 = [d.mi];
     d.leaderi.children.forEach(function (d2){
        varList1.push(d2);
      });
     varList2 = [d.mj];
      d.leaderj.children.forEach(function (d2){
        varList2.push(d2);
      }); 
  }  
  // Draw variable name
  svg_.selectAll(".varText23") 
    .data(varList1).enter()
    .append("g").attr("transform", function(d,i) {
      return "translate(" +(padding+10 + i*20) + "," + (padding -2) + ")"+ " rotate(-40)" 
    })
    .append("text")
      .attr("class", "varText23")
      .attr("x", 0)
      .attr("y", 0)
      .text(function(d,i) { return traits[d]; })
      .style("fill", function(d3) { 
        if (d3==p.mi)
          return "#000";  
        else
          return "#0a0";  
     });    

  svg_.selectAll(".varText24") 
    .data(varList2).enter()
    .append("g").attr("transform", function(d,i) {
      return "translate(" +(padding-3)+ "," + (padding+(i+1)*16) + ")"
    })
    .append("text")
      .style("text-anchor", "end")
      .attr("class", "varText24")
      .attr("x", 0)
      .attr("y", 0)
      .text(function(d,i) { return traits[d]; })
      .style("fill", function(d3) { 
        if (d3==p.mj)
          return "#000";  
        else
          return "#0a0";  
      });  
}



