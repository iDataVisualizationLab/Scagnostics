/* 2016 
 * Tommy Dang (on the Scagnostics project, as Assistant professor, iDVL@TTU)
 *
 * THIS SOFTWARE IS BEING PROVIDED "AS IS", WITHOUT ANY EXPRESS OR IMPLIED
 * WARRANTY.  IN PARTICULAR, THE AUTHORS MAKE NO REPRESENTATION OR WARRANTY OF ANY KIND CONCERNING THE MERCHANTABILITY
 * OF THIS SOFTWARE OR ITS FITNESS FOR ANY PARTICULAR PURPOSE.
 */

var width = 1200,
    size = 350,
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

//var file = "data/Breast";
//var file = "data/Sonar";
var file = "data/NRC";      // This is the data for Figure 6 in the paper
//var file = "data/Subway3";

//var file = "data2/MLB2008"; 
//var file = "data2/2016";  // Sample data of 3 variables for Figure 5 in the paper

//  d3.tsv("data/Subway3Standardized.csv", function(error, data_) {
d3.tsv(file+"Standardized.csv", function(error, data_) {
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
   d3.tsv(file+"Output2.csv", function(error, data2) {
    dataS = data2;


    var standard = gaussian(0.5, 0.2);


    function gaussian(mean, stdev) {
        var y2;
        var use_last = false;
        return function() {
            var y1;
            if(use_last) {
               y1 = y2;
               use_last = false;
            }
            else {
                var x1, x2, w;
                do {
                     x1 = 2.0 * Math.random() - 1.0;
                     x2 = 2.0 * Math.random() - 1.0;
                     w  = x1 * x1 + x2 * x2;               
                } while( w >= 1.0);
                w = Math.sqrt((-2.0 * Math.log(w))/w);
                y1 = x1 * w;
                y2 = x2 * w;
                use_last = true;
           }

           var retval = mean + stdev * y1;
           if(retval > 0) 
               return retval;
           return -retval;
       }
    }
    
    
    for (var i=0; i<data.length;i++){
      var j=0;
      var preData;
      for (var key in data[i]){
       // data[i][key]= Math.random();
        if (j==0){  // the first variable
          data[i][key]=i/(data.length-1);
        }
        else if (j==1){
          var x=i/(data.length-1);
         // if (i==63) x=0;
          var d = -x*20+10; 
          data[i][key] =  (0.05+(1/(1+Math.exp(d))))*0.92+(Math.random()-0.5)*0.04;
          if (x<0.5)
            data[i][key]*=0.8;
          else{
             data[i][key]=Math.sqrt(data[i][key]);
          }
        }
        else if (j==2){ 
          var v =  standard();
          if (v>1) v=1;
          if (v<0) v=0;

          var x=i/(data.length-1);
          // if (i==63) x=0;
         
          if (x<0.5)
            data[i][key] = v/1.3;
          else
            data[i][key] = 0.3+v/1.5;  
        }
        /*
        else if (j==5){ 
          var x=i/(data.length-1);
          if (x<0.5)
            x*=0.25;
          data[i][key] = Math.sqrt(x);
        }
        
        else if (j==6){  
          var v1 =  standard();
          if (v1>1) v1=1;
          if (v1<0) v1=0;
          var v2 =  standard();
          if (v2>1) v2=1;
          if (v2<0) v2=0;

          var x= Math.random();
          preData={};
          if (x<0.5){
            data[i][key] = v1/2;
            preData.group=1;
            preData.value = data[i][key];
          }  
          else{
            data[i][key] = 0.5+v2/2;  
            preData.group=2;
            preData.value = data[i][key];
          }
       //   console.log(data[i][key]);
        }*/
  
        j++; 
      }    
    }
    
    var text = "";
    for (var i=0; i<data.length;i++){
      var count=0;
      for (var key in data[i]){
        if (count==2 || count ==11)
          text+=data[i][key]+"\t";
        else if (count==29)
          text+=data[i][key];
        count++;
      } 
      text += "\n";
    }  
      console.log(text);     
    
      
  // drawScagHistogram(0, 200,200, size-50,size-120);
  // drawScagHistogram(1, 200,600, size-50,size-120);
  // drawScagHistogram(2, 600,600, size-50,size-120);
  // drawScagHistogram(getIndex(2,29), 200,200, size-50,size-120);
  // drawScagHistogram(getIndex(2,11), 200,600, size-50,size-120);
  // drawScagHistogram(getIndex(11,29), 600,600, size-50,size-120);


    leaderList = leaderAlgorithm(traits, disSimMonotonic);
    var pairList = cross();
       
 splomMain(svg, pairList, leaderList);
// findMostDifferent();

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
      var r = 0;
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