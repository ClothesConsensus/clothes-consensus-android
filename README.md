# Clothes Consensus

A video of the current app can be viewed here: https://youtu.be/j6XFYo-_Qck

Wireframe:
<img src='http://i.imgur.com/u1p4n03.jpg' title='Video Walkthrough' width='' alt='Video Walkthrough' />

Planning doc:
https://docs.google.com/document/d/1LOxPFthKg2wUkl2SDUod_5-D1DhNKHpguzYgspH2ct8/edit


<img src='http://i.imgur.com/e21Pdlx.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

Current functionality: http://i.imgur.com/e21Pdlx.gif


##Goals
###Sprint 1 Goals
* [x] Flow through entire app

* [x] Android models
* [x] Access profile or camera
* [x] The camera activity(built in camera)
* [x] All the UI elements
* [x] Fake data
* [x] UI element for timer?(Ryan)

###Sprint 2 Goals
* [x] Custom camera overlay (Ryan)
* [x] Swipe to vote (Shashank)
* [x] Backend/network (Ryan/Shashank)
* [x] Profile Activity (Shashank)
* [x] Push notifications (Shashank)

###Sprint 3 Goals
* [x] Profile page styling (Shashank)
* [x] Improve swiping (Ryan)
* [x] Navigation bar per pages (Ryan)
* [x] Activity transitions (Ryan)
* [x] Custom camera fully working (Ryan)
* [x] Custom slider to set time (Ryan)
* [x] Finish push notificiations (Shashank)


###Additional Styling Goals
* [x] Activity Transitions
* [x] Progress Bar
* [ ] Camera click press down
* [x] Swipe follow finger
* [x] Pinch to zoom
* [x] Login email


###Sprint 4
* [x] Success banner (for after posts and dropdown)
* [ ] Placeholder images
* [x] Load image before slide
* [x] Have exact ordering of demo looks set

* [x] Finish push
* [ ] Improve swipe
* [x] Prevent using from swiping second table view cell
* [ ] Icons on login buttons
* [ ] Aspect ratio during camera flow
* [ ] Reaction for nav button presses
* [x] Load first few images in login view for caching purposes
* [x] Get /me/ loading
* [x] User not load his own looks
* [x] Background color on profile page banner is slightly off
* [x] Banner when posting image is right


### Login (hardness: 1)
* Some sort of email auth
* Facebook auth
* Twitter auth
* Concept of a user

### Main Listing/Voting (hardness: 3)
* Customer Adapter
* Action bar
* Dual Look view
* Single Look view
* Flag for inappropriate

### Camera (hardness: 5)
* Store image into local storage/gallery
* Custom overlay
* Dual photo capability
* Rotate camera
* Turn flash on
* Accessing the gallery
* Confirmation

### Camera Confirmation (after photo is taken) : details of look (hardness: 3.5)
* Networking call to POST the photo
* Ability to set timer
* Load image from local storage or gallery
* Add caption
* Share to email/FB/twitter - nice to have

### History of your past looks (Not yet designed) (hardness: 3.5)
* User details/profile
* Username
* Profile image
* No of looks posted(nice to have)

### Adapter
* Single look view (with asking message & result)
* Dual look view
	
### Push notification (not a screen) (hardness: ???)


##Models
* User
	* Has many looks
	* Has many votes
* Look
	* Has one User
	* Many Votes
	* Expiration time
	* Question text?

* Vote
	* Has one user
	* Has one look

* Look vs Look
	* Join to looks that are being compared



