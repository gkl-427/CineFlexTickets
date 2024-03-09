import Ember from 'ember';

export default Ember.Route.extend({
    actions: {
        willTransition() {
          const controller = this.controller;
          controller.set('description','');
          controller.set('casting','');
          controller.set('selectedLanguage','');
          controller.set('genre','');
          controller.set('currentDate','');
        },
        redirectToParent() {
              this.transitionToRoute('dashboard.add-movie');
            }
      }
});
