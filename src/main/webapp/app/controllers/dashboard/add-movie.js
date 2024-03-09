import Ember from 'ember';
import ENV from '../../config/environment';

export default Ember.Controller.extend({
    currentUser: Ember.inject.service(),
    today:new Date().toISOString().slice(0, 10),

    isManager: Ember.computed('currentUser.role', function () {
        return this.get('currentUser').get('role') == 2;
    }),

    moviename: '',
    description: '',
    casting: '',
    selectedLanguage: null,
    genre: '',
    moviedate: null,
    availableLanguages: [
        { name: '' },
        { name: 'Tamil' },
        { name: 'English' },
        { name: 'Malayalam' },
        { name: 'Telugu' },
        { name: 'Kannada' },
        { name: 'Hindi' },

    ],
    availableGenres: ['Action', 'Adventure', 'Comedy', 'Drama', 'Fantasy', 'Horror', 'Romance', 'Sci-Fi', 'Thriller'],
    actions: {
        updateDate(date) {
            this.set('currentDate', date);
        },
        selectLanguage(name) {
            this.set('selectedLanguage', name);
        },
        sendRecord() {
            let formData = {
                moviename: this.get('moviename'),
                description: this.get('description'),
                casting: this.get('casting'),
                language: this.get('selectedLanguage'),
                genre: this.get('genre'),
                moviedate: this.get('currentDate')
            };
            Ember.$.ajax({
                url: ENV.APP.API_URL + '/movies',
                type: 'POST',
                data: JSON.stringify(formData),
                dataType: 'application/json'
            }).then(() => {
                let $element = Ember.$('.element-to-animate');
                $element.addClass('fade-out');

                Ember.run.later(() => {
                    this.transitionToRoute('dashboard.movieslist');
                }, 500);
            }).catch(() => {
                let $element = Ember.$('.element-to-animate');
                $element.addClass('fade-out');

                Ember.run.later(() => {
                    this.transitionToRoute('dashboard.movieslist');
                }, 500);
            })
        }

    }
});


