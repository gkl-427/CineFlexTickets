import Ember from 'ember';
import config from './config/environment';

const Router = Ember.Router.extend({
  location: config.locationType,
  rootURL: config.rootURL+'ticket-app/'
});

Router.map(function() {
  this.route('movies');
  this.route('signup');
  this.route('login');
  this.route('not-found',{path :'/*path',templateName: 'not-found'});
  this.route('app');
  this.route('theatres',{ path: '/theatre/:movie_id' });
  this.route('dashboard', function() {
    this.route('movieslist');
    this.route('add-movie');
    this.route('theatres', function() {});
    this.route('add-show');
    this.route('shows');
    this.route('theatredetails');
    this.route('user');
    this.route('bookings');
    this.route('users');
    this.route('add-theatre');
    this.route('listalltheatres');
    this.route('addseats');
  });
  this.route('components');
  this.route('booktickets',{path: '/book-ticket/:theatreid/:showid'});
});

export default Router;
