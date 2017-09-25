

angular.module('infocomApp').controller('BashController', ['$scope', '$timeout', 'BashService', function($scope, $timeout, BashService){

    var interval = window.setInterval(showAllBash, 8000);
    var self = this;
    self.bash = {
    id:null,
    script: '',
    timedate: null,
    frequency: null,
    nextexecution: null
    }

    self.settings = {
    email:'',
    password:'',
    text:''
    }

    self.page = {
    pageNumber: 0,
    size: 2,
    totalPages: 0
    }


    self.bashscripts = [];

   self.submit = submit;
   self.edit = edit;
   self.remove = remove;
   self.reset = reset;
   self.useTask = useTask;
   self.changeEmailSettings = changeEmailSettings;
   self.open = open;
   self.selectPage = selectPage;
   self.selectNumberPerPage = selectNumberPerPage;

   showEmailSettings();
   showAllBash();


   function changeApi(taskNumber, task, content){
   console.log('Ok ');
    BashService.changeApi(taskNumber);
             $timeout(function(){
             showAllBash();
               $scope.$apply()
             },0)
             document.getElementById("task").innerHTML = task;
             document.getElementById("header").innerHTML = content;
             document.getElementById("taskLabel").innerHTML = task;
             console.log(self.bashscripts);

    }
   function useTask(taskNumber, task, content){
   console.log('Great ');
   changeApi(taskNumber, task, content);
   }


   function makeEmailChanges(settings){
   BashService.makeEmailChanges(settings);
   }
   function changeEmailSettings(){
   makeEmailChanges(self.settings);
   }
   function showEmailSettings(){
   BashService.showEmailSettings().
   then(
   function(result){
        self.settings = result;
        console.log(result)},
        function(errResponse){
        alert("Error finding Email settings");
        }
        );
    }

    function selectPage(page){
    self.page.pageNumber = page;
    showAllBash();
    }

    function selectNumberPerPage(numberPerPage){
    self.page.size = numberPerPage;
    self.page.pageNumber =0;
    showAllBash();
    }

   function showAllBash(){
    BashService.showAllBash(self.page.pageNumber,self.page.size)
        .then(
        function(result){
            self.bashscripts = result.content;
            self.page.totalPages = result.totalPages;

            $scope.content = result.content;
            console.log($scope.content);
            $timeout(function(){
                           $scope.$apply()
                         },500)
            },
            function(errResponse){
            window.clearInterval(interval);
            alert('Error finding Bash-scripts');
            }
         );
     }

     function createBash(bash){
             BashService.createBash(bash)
                 .then(
                 showAllBash,
                 function(errResponse){
                     alert('Error while creating Bash-scripts');
                 }
             );
         }
    function updateBash(bash, id){
        BashService.updateBash(bash, id)
            .then(
            showAllBash,
            function(errResponse){
                alert('Error while updating Bash-scripts');
            }
        );
    }

    function deleteBash(id){
        BashService.deleteBash(id)
            .then(
            showAllBash,
            function(errResponse){
                alert('Error while deleting Bash-scripts');
            }
        );
    }

    function submit() {
        if(self.bash.id===null){
         console.log(self.bash.timedate);
            self.bash.nextexecution = self.bash.timedate;
            if(self.bashscripts.length == 0) {
                self.bash.id = 1;
            } else {
                self.bash.id = self.bashscripts[self.bashscripts.length-1].id + 1;
            }
            console.log('Saving New Bash-script', self.bash);
            createBash(self.bash);
        }else{
            updateBash(self.bash, self.bash.id);
            console.log('Bash-script updated with id ', self.bash.id);
        }
        reset();
    }

    function edit(id){
        alert('id to be edited', id);
        for(var i = 0; i < self.bashscripts.length; i++){
            if(self.bashscripts[i].id === id) {
                self.bash = angular.copy(self.bashscripts[i]);
                break;
            }
        }
    }

    function remove(id){
        alert('id to be deleted', id);
        if(self.bash.id === id) {//clean form if the user to be deleted is shown there.
            reset();
        }
        deleteBash(id);
    }


    function reset(){
        self.bash = {id:null,
                   script:'',
                   timedate: null,
                   frequency: null,
                   nextexecution: null};
        $scope.myForm.$setPristine();//reset Form

    }





}]);



