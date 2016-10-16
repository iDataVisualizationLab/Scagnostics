var width = 1200,
    size = 80,
    padding = size/20;

var x = d3.scale.linear()
    .range([padding , size - padding]);
var y = d3.scale.linear()
    .range([size - padding , padding ]);
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
var domainByTrait= ["0.0", "1.0"];
var colorRedBlue = d3.scale.linear()
    .domain([0, 0.4, 1])
    .range(["#55f", "white", "red"]);
var leaderList;
var traits;    
var data, dataS;    


//d3.tsv("data/SonarStandardized.csv", function(error, data) {
d3.tsv("data/BreastStandardized.csv", function(error, data_) {
  if (error) throw error;

  data = data_
  traits = d3.keys(data[0]).filter(function(d) { return d !== ""; });
  var n = traits.length;

  var brush = d3.svg.brush()
      .x(x)
      .y(y)
      .on("brushstart", brushstart)
      .on("brush", brushmove)
      .on("brushend", brushend);

  var svg = d3.select("body").append("svg")
      .attr("width", size * n + padding)
      .attr("height", size * n + padding)
      .append("g")
        .attr("transform", "translate(" + padding + "," + padding / 2 + ")");

  

   svg.append("text")
      .attr("class", "textNotification")
      .attr("x", padding)
      .attr("y", padding)
      .text("Finished reading data points");
   
    svg.call(tip);       

  // Reading Scagnostics data ***********************************************************
  d3.tsv("data/BreastOutput2.csv", function(error, data2) {
    dataS = data2;
    svg.append("text")
      .attr("class", "textNotification")
      .attr("x", padding)
      .attr("y", padding+14)
      .text("Finished reading Scagnostics");


    leaderList = leaderAlgorithm(traits, disSim);
    var varList = cross();
    splom(svg, varList);

    function cross() {
      var c = [], n = leaderList.length, i, j;
      for (i = 0; i < n; i++) 
        for (j = 0; j < n; j++) {
          var miLeader = leaderList[i].mi;
          var mjLeader = leaderList[j].mi;
          c.push({x: traits[miLeader], i: i, y: traits[mjLeader], j: j,
           mi: miLeader, mj: mjLeader, leaderi: leaderList[i], leaderj: leaderList[j]});
        }
      return c;
    }

    function disSim(mi, mj){
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

    // Implementation of leader algorithm
    // arr: input variables
    // sim: similarity funciton
    function leaderAlgorithm(arr, disSim){
      var r = 0.5;
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