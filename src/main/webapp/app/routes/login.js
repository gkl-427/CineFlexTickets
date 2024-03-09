import Ember from 'ember';

export default Ember.Route.extend({
  currentUser: Ember.inject.service(),
  beforeModel() {
    if (this.get('currentUser.isAuthenticated')) {
      this.transitionTo('dashboard');
    }
  },
  actions: {
    willTransition() {
      const controller = this.controller;
      controller.set('mobilenumber', '');
      controller.set('password', '');
    }
  }
});
