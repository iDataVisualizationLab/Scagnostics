/* 2016 
 * Tommy Dang (on the Scagnostics project, as Assistant professor, iDVL@TTU)
 *
 * THIS SOFTWARE IS BEING PROVIDED "AS IS", WITHOUT ANY EXPRESS OR IMPLIED
 * WARRANTY.  IN PARTICULAR, THE AUTHORS MAKE NO REPRESENTATION OR WARRANTY OF ANY KIND CONCERNING THE MERCHANTABILITY
 * OF THIS SOFTWARE OR ITS FITNESS FOR ANY PARTICULAR PURPOSE.
 */

var width = 1200,
    size = 50,
    padding = size/12;

var x = d3.scale.linear()
    .range([padding , size - padding]);
var y = d3.scale.linear()
    .range([size - padding , padding ]);

var domainByTrait= ["0.0", "1.0"];
x.domain(domainByTrait);
y.domain(domainByTrait);

var xAxis = d3.svg.axis()
    .scale(x)
    .orient("bottom")
    .ticks(3);
var yAxis = d3.svg.axis()
    .scale(y)
    .orient("left")
    .ticks(3);
var color = d3.scale.category10();

//******************************

var colorRedBlue = d3.scale.linear()
    .domain([0, 0.5, 1])
    .range(["#00f", "white", "#f00"]);
var leaderList;
var traits;    
var data, dataS;    
var svg;


d3.tsv("data/BreastStandardized.csv", function(error, data_) {
//d3.tsv("data/SonarStandardized.csv", function(error, data_) {
//  d3.tsv("data/NRCStandardized.csv", function(error, data_) {
  if (error) throw error;

  data = data_;
  traits = d3.keys(data[0]).filter(function(d) { return d !== ""; });
  var n = traits.length;

  var brush = d3.svg.brush()
    .x(x)
    .y(y)
    .on("brushstart", brushstart)
    .on("brush", brushmove)
    .on("brushend", brushend);

  svg = d3.select("body").append("svg")
    .attr("width", size * n + padding)
    .attr("height", size * n + padding)
    .append("g")
    .attr("transform", "translate(" + padding + "," + padding / 2 + ")");

  svg.append("text")
    .attr("class", "textNotification")
    .attr("x", padding)
    .attr("y", 0)
    .text("Finished reading data points");
   
    svg.call(tip);       

  // Reading Scagnostics data ***********************************************************
  d3.tsv("data/BreastOutput2.csv", function(error, data2) {
  //d3.tsv("data/SonarOutput2.csv", function(error, data2) {
  //d3.tsv("data/NRCOutput2.csv", function(error, data2) {
    dataS = data2;
    svg.append("text")
      .attr("class", "textNotification")
      .attr("x", padding)
      .attr("y", 12)
      .text("Finished reading Scagnostics");


    leaderList = leaderAlgorithm(traits, disSim);
    var pairList = cross();
    splomMain(svg, pairList, leaderList);

    function cross() {
      var c = [], n = leaderList.length, i, j;
      for (i = 0; i < n; i++) 
        for (j = i+1; j < n; j++) {
          var miLeader = leaderList[i].mi;
          var mjLeader = leaderList[j].mi;
          c.push({x: traits[miLeader], i: i, y: traits[mjLeader], j: j,
           mi: miLeader, mj: mjLeader, leaderi: leaderList[i], leaderj: leaderList[j]});
        }
      return c;
    }

    function disSimMonotonic(mi, mj){
      if (mi<mj){
         var k = mj*(mj-1)/2+mi; 
         return 1-dataS[k]["Monotonic"];
      }
      else if (mi>mj){
        var k = mi*(mi-1)/2+mj; 
        return 1-dataS[k]["Monotonic"];
      }
      else{
        return 0; // Monotonic==1 if same variable for x and y
      }
    }  

    function disSim(mi, mj){
      var dif=0;
      for (var i=0; i<n; i++){
        if (i==mi || i==mj) continue;
        var indexI = getIndex(i,mi);
        var indexJ = getIndex(i,mj);
        
        dif += Math.abs(dataS[indexI]["Outlying"]-dataS[indexJ]["Outlying"]);
        dif += Math.abs(dataS[indexI]["Skewed"]-dataS[indexJ]["Skewed"]);
        dif += Math.abs(dataS[indexI]["Clumpy"]-dataS[indexJ]["Clumpy"]);
        dif += Math.abs(dataS[indexI]["Sparse"]-dataS[indexJ]["Sparse"]);
        dif += Math.abs(dataS[indexI]["Striated"]-dataS[indexJ]["Striated"]);
        dif += Math.abs(dataS[indexI]["Convex"]-dataS[indexJ]["Convex"]);
        dif += Math.abs(dataS[indexI]["Skinny"]-dataS[indexJ]["Skinny"]);
        dif += Math.abs(dataS[indexI]["Stringy"]-dataS[indexJ]["Stringy"]);
        dif += Math.abs(dataS[indexI]["Monotonic"]-dataS[indexJ]["Monotonic"]);       
      }
      return dif/(n-2);
      //console.log(dif);
    }  
    
    // Implementation of leader algorithm
    // arr: input variables
    // sim: similarity funciton
    function leaderAlgorithm(arr, disSim){
      var r = 0.65;
      var leaderList = [];
      for (var i=0; i< arr.length; i++){
        var minDis = 10000;
        var minIndex = -1;
        // Finding the leader
        for (var l=0; l< leaderList.length; l++){
          var dis = disSim(i,leaderList[l].mi);
          if (dis<minDis){
            minDis=dis;
            minIndex = l;
          }
        }  
        // Checking 
        if (minIndex>=0 && minDis<r){   // If found a leader (with minDis)
          var oldLeader = leaderList[minIndex];
          oldLeader.children.push(i);
        }
        else{
          var newLeader = {};
          newLeader.mi = i;
          newLeader.children = []; 
          leaderList.push(newLeader);
        }  
      }
      return leaderList;
    }
  });  
});