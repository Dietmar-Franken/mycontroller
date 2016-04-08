/*
 * Copyright (C) 2015-2016 Jeeva Kandasamy (jkandasa@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
myControllerModule.controller('RoomsControllerList', function(alertService,
$scope, RoomsFactory, $state, $uibModal, displayRestError, mchelper, CommonServices, $filter) {

  //GUI page settings
  $scope.headerStringList = $filter('translate')('ROOMS_DETAIL');
  $scope.noItemsSystemMsg = $filter('translate')('NO_ROOMS_SETUP');
  $scope.noItemsSystemIcon = "fa fa-object-group";

  //load empty, configuration, etc.,
  $scope.mchelper = mchelper;
  $scope.filteredList=[];
    
  //data query details
  $scope.currentPage = 1;
  $scope.query = CommonServices.getQuery();
  $scope.queryResponse = {};
  
  //Get min number
  $scope.getMin = function(item1, item2){
    return CommonServices.getMin(item1, item2);
  };

  //get all Sensors
  $scope.getAllItems = function(){
    RoomsFactory.getAll($scope.query, function(response) {
      $scope.queryResponse = response;
      $scope.filteredList = $scope.queryResponse.data;
      $scope.filterConfig.resultsCount = $scope.queryResponse.query.filteredCount;
    },function(error){
      displayRestError.display(error);
    });
  }

  //Hold all the selected item ids
  $scope.itemIds = [];

  $scope.selectAllItems = function(){
    CommonServices.selectAllItems($scope);
  };

  $scope.selectItem = function(item){
    CommonServices.selectItem($scope, item);
  };
  
  //On page change
  $scope.pageChanged = function(newPage){
    CommonServices.updatePageChange($scope, newPage);
  };

  //Filter change method
  var filterChange = function (filters) {
    //Reset filter fields and update items
    CommonServices.updateFiltersChange($scope, filters);
  };
  
  $scope.filterConfig = {
    fields: [
      {
        id: 'name',
        title: $filter('translate')('NAME'),
        placeholder: $filter('translate')('FILTER_BY_NAME'),
        filterType: 'text'
      },
      {
        id: 'description',
        title: $filter('translate')('DESCRIPTION'),
        placeholder: $filter('translate')('FILTER_BY_DESCRIPTION'),
        filterType: 'text',
      }
    ],
    resultsCount: $scope.filteredList.length,
    appliedFilters: [],
    onFilterChange: filterChange
  };
  
  //Sort columns
  var sortChange = function (sortId, isAscending) {
    //Reset sort type and update items
    CommonServices.updateSortChange($scope, sortId, isAscending);
  };

  $scope.sortConfig = {
    fields: [
      {
        id: 'name',
        title:  $filter('translate')('NAME'),
        sortType: 'text'
      },{
        id: 'description',
        title:  $filter('translate')('DESCRIPTION'),
        sortType: 'text'
      }
    ],
    onSortChange: sortChange
  };

    
  //Edit item
  $scope.edit = function () {
    if($scope.itemIds.length == 1){
      $state.go("roomsAddEdit", {'id':$scope.itemIds[0]});
    }
  };
  
  //Delete item(s)
  $scope.delete = function (size) {
    var modalInstance = $uibModal.open({
    templateUrl: 'partials/common-html/delete-modal.html',
    controller: 'ControllerDeleteModal',
    size: size,
    resolve: {}
    });

    modalInstance.result.then(function () {
      RoomsFactory.deleteIds($scope.itemIds, function(response) {
        alertService.success($filter('translate')('ITEMS_DELETED_SUCCESSFULLY'));
        //Update display table
        $scope.getAllItems();
        $scope.itemIds = [];
      },function(error){
        displayRestError.display(error);
      }); 
    }), 
    function () {
      //console.log('Modal dismissed at: ' + new Date());
    }
  };

});


//Add Edit item controller
myControllerModule.controller('RoomsControllerAddEdit', function ($scope, $stateParams, $state, RoomsFactory, TypesFactory,  mchelper, alertService, displayRestError, $filter) {
  //GUI page settings
  $scope.showHeaderUpdate = $stateParams.id;
  $scope.headerStringAdd = $filter('translate')('ADD_ROOM');
  $scope.headerStringUpdate = $filter('translate')('UPDATE_ROOM');
  $scope.cancelButtonState = "roomsList"; //Cancel button state
  $scope.saveProgress = false;
  //$scope.isSettingChange = false;
  
  $scope.mchelper = mchelper;
  $scope.item = {};
  $scope.sensorIds = [];
  $scope.sensors = {};
  $scope.id = $stateParams.id;

 
  if($stateParams.id){
    RoomsFactory.get({"id":$stateParams.id},function(response) {
        $scope.item = response;
      },function(error){
        displayRestError.display(error);
      });
    $scope.sensors = TypesFactory.getSensors({"roomId":$stateParams.id, "enableNoRoomFilter":true});
  }else{
    $scope.sensors = TypesFactory.getSensors({"enableNoRoomFilter":true});
  }
  
  $scope.save = function(){
    $scope.saveProgress = true;
    if($stateParams.id){
      RoomsFactory.update($scope.item,function(response) {
        alertService.success($filter('translate')('ITEM_UPDATED_SUCCESSFULLY'));
        $state.go("roomsList");
      },function(error){
        displayRestError.display(error);
        $scope.saveProgress = false;
      });
    }else{
      RoomsFactory.create($scope.item,function(response) {
        alertService.success($filter('translate')('ITEM_CREATED_SUCCESSFULLY'));
        $state.go("roomsList");
      },function(error){
        displayRestError.display(error);
        $scope.saveProgress = false;
      });
    }
  }
});
