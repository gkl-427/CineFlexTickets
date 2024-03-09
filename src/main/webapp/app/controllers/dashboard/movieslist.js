import Ember from 'ember';

export default Ember.Controller.extend({

    currentUser: Ember.inject.service(),

    isManager: Ember.computed('currentUser.role', function () {
        return this.get('currentUser').get('role') === 2;
    }),

});
