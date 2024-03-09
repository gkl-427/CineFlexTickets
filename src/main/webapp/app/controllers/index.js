import Ember from 'ember';

export default Ember.Controller.extend({
    currentUser: Ember.inject.service(),
    isAuthenticate: Ember.computed('currentUser.isAuthenticated', function() {
        return this.get('currentUser').get('isAuthenticated');
    })
});
