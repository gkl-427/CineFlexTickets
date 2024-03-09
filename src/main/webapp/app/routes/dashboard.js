import Ember from 'ember';

export default Ember.Route.extend({
    currentUser: Ember.inject.service(),
    beforeModel() {
        if (!this.get('currentUser.isAuthenticated')) {
            this.transitionTo('login');
        }
    },
    isOpen: false,
    actions: {
      toggleDropdown() {
        this.toggleProperty('isOpen');
      }
    }
});
