/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 */

import { NewAppScreen } from '@react-native/new-app-screen';
import React from 'react';
import { Storyly } from 'storyly-react-native';
import { StatusBar, StyleSheet, useColorScheme, View } from 'react-native';

const TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhY2NfaWQiOjc2MCwiYXBwX2lkIjo0MDUsImluc19pZCI6NDA0fQ.1AkqOy_lsiownTBNhVOUKc91uc9fDcAxfQZtpm3nj40";

function App() {
  const isDarkMode = useColorScheme() === 'dark';

  return (
    <View style={styles.container}>
      <StatusBar barStyle={isDarkMode ? 'light-content' : 'dark-content'} />
      <Storyly
        style={{ height: "100%" }}
        storylyId={TOKEN}
        onLoad={event => { console.log(event); }}
        onFail={event => { console.log("[Storyly] onFail"); }}
        onPress={event => { console.log("[Storyly] onPress"); }}
        onEvent={event => { console.log("[Storyly] onEvent"); }}
        onStoryOpen={() => { console.log("[Storyly] onStoryOpen"); }}
        onStoryClose={() => { console.log("[Storyly] onStoryClose"); }}
        onUserInteracted={event => { console.log("[Storyly] onUserInteracted"); }}
      />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
});

export default App;
