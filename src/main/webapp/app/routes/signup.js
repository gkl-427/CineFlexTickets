import Ember from 'ember';

export default Ember.Route.extend({
    actions: {
        willTransition() {
          const controller = this.controller;
          controller.set('firstname', '');
          controller.set('lastname', '');
          controller.set('mobilenumber', '');
          controller.set('confirmPassword','');
          controller.set('password','');
          controller.set('address','');
        }
      }
});
