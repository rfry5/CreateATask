# CreateATask

## Setting up a local developer environment

### Install the Software
The following assumes you are on a Mac.
If that is not the case, then you might need to change up some of these instructions. 

1. Install [Homebrew](http://brew.sh/) -    
`/usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"`
2. Tap Homebrew Cask -    
`brew tap caskroom/cask`
3. Install Java 8 -    
`brew cask install java` 
4. Install [Android Studio](https://developer.android.com/studio/install.html) -     
`brew cask install android-studio`

### Checkout the code and setup Android Studio

1. Checkout CreateATask -    
`cd ~/Documents && git checkout git@github.com:rfry5/CreateATask.git`
2. Open Android Studio and Import the project
    * Use the "Import Project" option and select the directory where you checked out CreateATask 
      (in your Documents folder if you are following directions)
    * When you import, you will get a Warning Message saying "Unregistered VCS root detected".
      This is ok - click the "Add root" button.
3. Happy Developing! 
