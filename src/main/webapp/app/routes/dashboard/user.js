import Ember from 'ember';
import ENV from '../../config/environment';

export default Ember.Route.extend({
    currentUser:Ember.inject.service(),
    model() {
        let userId=this.get('currentUser.userId');
        return Ember.$.getJSON({
            url: ENV.APP.API_URL + '/users/'+userId,
            type: 'GET',
            dataType: 'json'
        })    
    }

});
