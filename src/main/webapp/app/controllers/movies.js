import Ember from 'ember';

export default Ember.Controller.extend({
    currentUser: Ember.inject.service(),

    isAuthenticate: Ember.computed('currentUser.isAuthenticated', function () {
      return this.get('currentUser').get('isAuthenticated');
    }),

    actions: {
      logout() {
        this.get('currentUser').logout();
        this.transitionToRoute('application');
      }
    },
    isManager: Ember.computed('currentUser.role', function () {
      return this.get('currentUser').get('role') === 2;
    }),
  
    isAdmin: Ember.computed('currentUser.role', function () {
      return this.get('currentUser').get('role') == 3;
    }),
    
});
