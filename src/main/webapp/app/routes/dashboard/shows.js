import Ember from 'ember';
import ENV from '../../config/environment';

export default Ember.Route.extend({
    currentUser: Ember.inject.service(),
    model() {
        let theatreid = this.get('currentUser.theatreId');
        console.log(theatreid);
        return Ember.$.getJSON({
            url: ENV.APP.API_URL + '/theatres/' + theatreid + '/shows?includemovies',
            type: 'GET',
            dataType: 'json'
        })
    },
});
