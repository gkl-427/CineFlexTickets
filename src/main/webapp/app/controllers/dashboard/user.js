import Ember from 'ember';

export default Ember.Controller.extend({
    currentUser: Ember.inject.service(),
    isManager: Ember.computed('currentUser.role', function () {
        return this.get('currentUser.role') == 2;
    }),
    Details: Ember.computed('currentUser.userdata' , function(){
        return this.get('currentUser.userdata');
    }),
});
