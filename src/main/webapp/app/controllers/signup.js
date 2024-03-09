import Ember from 'ember';
import ENV from '../config/environment';

export default Ember.Controller.extend({

  passwordMismatch: false,
  passwordLength: false,
  mobileNumberError:false,
  credentials:false,

  passwordObserver: Ember.observer('password', 'confirmPassword', function () {
    let password = this.get('password');
    let confirmPassword = this.get('confirmPassword');

    if (password !== confirmPassword) {
      this.set('passwordMismatch', true);
    } else {
      this.set('passwordMismatch', false);
    }
  }),
  passwordLengthObserver: Ember.observer('password', 'confirmPassword', function () {
    let password = this.get('password');
    let confirmPassword = this.get('confirmPassword');

    if (password.length <= 6 && confirmPassword.length <= 6) {
      this.set('passwordLength', true);
    } else {
      this.set('passwordLength', false);
    }
  }),


  mobilenumberObserver:Ember.observer('mobilenumber',function() {
    let mobileNumber = this.get('mobilenumber');
    let mobileNumberRegex = /^\d{10}$/; 
    if (!mobileNumberRegex.test(mobileNumber)) {
      this.set('mobileNumberError', true);
    }else{
      this.set('mobileNumberError',false);
    }
  }),

  actions: {
    sendRecord: function () {
      let firstname = this.get('firstname');
      let lastname = this.get('lastname');
      let mobilenumber = this.get('mobilenumber');
      let password = this.get('password');
      let address = this.get('address');
      const user = {
        firstname: firstname,
        lastname: lastname,
        mobilenumber: mobilenumber,
        password: password,
        address: address
      };
      Ember.$.ajax({
        url: ENV.APP.API_URL + '/users',
        type: 'POST',
        data: JSON.stringify(user)
      }).then(response => {
        if (response.success) {
          let $element = Ember.$('.element-to-animate');
          $element.addClass('fade-out');

          Ember.run.later(() => {
            this.transitionToRoute('login');
          }, 500);
        }
        else {
          alert('Something Went Wrong');
          this.transitionToRoute('signup')
        }
      }).catch(response=> {
        if(!response.success){
          alert('Something went wrong.May be user credentials already exists')
          this.transitionToRoute('signup')
        }
      });

    },
  }
});
