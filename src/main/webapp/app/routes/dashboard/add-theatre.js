import Ember from 'ember';
import ENV from '../../config/environment';

export default Ember.Route.extend({
    currentUser: Ember.inject.service(),
    isAdmin: Ember.computed('currentUser.role', function () {
        return this.get('currentUser').get('role') == 3;
    }),

    model(){
        return Ember.$.getJSON({
            url: ENV.APP.API_URL + '/users/?role=1',
            type: 'GET',
            dataType: 'json'
        })
    }
});
