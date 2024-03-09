import Ember from 'ember';

export default Ember.Controller.extend({
    isTheatreAvailable:false,
    filteredData: null,
    isManager: Ember.computed('currentUser.role', function () {
      return this.get('currentUser').get('role') === 2;
    }),
  
    isAdmin: Ember.computed('currentUser.role', function () {
      return this.get('currentUser').get('role') == 3;
    }),

    currentUser: Ember.inject.service(),

    isAuthenticate: Ember.computed('currentUser.isAuthenticated', function () {
      return this.get('currentUser').get('isAuthenticated');
    }),
    actions:{
        filtertheatre(){
            let searchQuery = this.get("searchQuery");
            let filteredData = this.get("model").filter((model) => {
              return model.theatrecity.toLowerCase().includes(searchQuery.toLowerCase());
            });
            this.set("filteredData", filteredData);
            if (filteredData.length==0 && searchQuery != null) {
              this.set("isTheatreAvailable", false);
            } else if (filteredData.length !=0) {
              this.set("isTheatreAvailable", true);
            }
        },
        
          logout() {
            this.get('currentUser').logout();
            this.transitionToRoute('application');
          }
        }
});
