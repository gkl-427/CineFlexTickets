import Ember from 'ember';
import ENV from '../../config/environment';

export default Ember.Controller.extend({

    userid:null,
    today:new Date().toISOString().slice(0, 10),
    currentUser: Ember.inject.service(),
    isAdmin: Ember.computed('currentUser.role', function () {
        return this.get('currentUser').get('role') == 3;
    }),
    actions:
    {
        selectmanager(Value) {
            this.set('userid', Value);
        },
        sendRecord() {
            let formData = {
                theatrename: this.get('theatrename'),
                theatrecity: this.get('theatrecity'),
                features: this.get('features'),
                userid: this.get('userid'),
            };
            Ember.$.ajax({
                url: ENV.APP.API_URL + '/theatres',
                type: 'POST',
                data: JSON.stringify(formData),
                dataType: 'application/json'
            }).then(response => {
                if (response.success) {
                    let $element = Ember.$('.element-to-animate');
                    $element.addClass('fade-out');

                    Ember.run.later(() => {
                        this.transitionToRoute('dashboard.addseats');
                    }, 500);
                } else {
                    alert('Something Went Wrong');
                }
            }).catch(() => {
                let $element = Ember.$('.element-to-animate');
                $element.addClass('fade-out');

                Ember.run.later(() => {
                    this.transitionToRoute('dashboard.addseats');
                }, 500);

            });
        },
       
    }
});
