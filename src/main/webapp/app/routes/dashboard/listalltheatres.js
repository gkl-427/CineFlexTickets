import Ember from 'ember';
import ENV from '../../config/environment';

export default Ember.Route.extend({
    currentUser: Ember.inject.service(),
    model() {
        return Ember.$.getJSON({
            url: ENV.APP.API_URL + '/theatres?includeusers',
            type: 'GET',
            dataType: 'json'
        })
    }
});
