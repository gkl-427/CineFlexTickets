import Ember from 'ember';
import ENV from '../config/environment';

export default Ember.Route.extend({

  actions: {
    redirectToTheatres(movieid) {
      this.transitionToRoute('movies.theatres', movieid);
    }
  },
  model() {
    let currentDate = new Date();
    return Ember.$.getJSON(ENV.APP.API_URL + '/movies').then(movies => {
      return movies.filter(movie => new Date(movie.moviedate) >= currentDate);
    });
  }
});
