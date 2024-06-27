import 'dart:math';

import 'package:flutter/material.dart';
import 'package:storyly_flutter/storyly_flutter.dart';

void main() {
  runApp(MainApp());
}

class MainApp extends StatefulWidget {
  MainApp({super.key});

 StorylyParam param = StorylyParam()
          ..storylyId = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY2NfaWQiOjU1NiwiYXBwX2lkIjoxMzg5LCJpbnNfaWQiOjE4NjY1fQ._PwkZ48JdHkSU01KUR2n66zJcL29JhykNTMRUorfvE4";

  @override
  State<MainApp> createState() => MainAppState();
}

class MainAppState extends State<MainApp> {

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        body: 
            Column(children: [
              StorylyWrapper(key: UniqueKey(), params: widget.param),
              TextButton(
                style: ButtonStyle(
                  foregroundColor: MaterialStateProperty.all<Color>(Colors.blue),
                ),
                onPressed: () { 
                  setState(() {
                    widget.param = StorylyParam()
                      ..storylyId = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY2NfaWQiOjU1NiwiYXBwX2lkIjoxMzg5LCJpbnNfaWQiOjE4NjY1fQ._PwkZ48JdHkSU01KUR2n66zJcL29JhykNTMRUorfvE4"
                      ..storylySegments = ["test-label-${1+Random().nextInt(2)}"];
                  });   
                },
                child: Text('Refresh'),
              ),
              Text(widget.param.storylySegments.toString())
             ],)

      ),
    );
  }
}

class StorylyWrapper extends StatefulWidget {
  StorylyParam params;

  StorylyWrapper({super.key, required this.params});

  @override
  _StorylyWrapperState createState() => _StorylyWrapperState();
}

class _StorylyWrapperState extends State<StorylyWrapper> {
  StorylyViewController? storylyViewController;

  void onStorylyViewCreated(StorylyViewController storylyViewController) {
    this.storylyViewController = storylyViewController;
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      height: 120,
      child: StorylyView(
        onStorylyViewCreated: onStorylyViewCreated,
        androidParam: StorylyParam()..storylyId = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY2NfaWQiOjIzODAsImFwcF9pZCI6MTcxODUsImluc19pZCI6MTkxMDB9.AmtkzTlj_g3RQwwHZTz6rsozH8VFqAogeSwgBdXLMDU",
        iosParam: widget.params,
        storylyLoaded: (storyGroups, dataSource) {
          debugPrint("storylyLoaded -> storyGroups: ${storyGroups.length}");
          debugPrint("storylyLoaded -> dataSource: $dataSource");
        },
        storylyLoadFailed: (errorMessage) => debugPrint("storylyLoadFailed: $errorMessage"),
        storylyActionClicked: (story) {
          debugPrint("storylyActionClicked -> ${story.title}");
        },
        storylyEvent: (event, storyGroup, story, storyComponent) {
          debugPrint("storylyEvent -> event: $event");
          debugPrint("storylyEvent -> storyGroup: ${storyGroup?.title}");
          debugPrint("storylyEvent -> story: ${story?.title}");
          debugPrint("storylyEvent -> storyComponent: $storyComponent");
        },
        storylyStoryShown: () => debugPrint("storylyStoryShown"),
        storylyStoryDismissed: () => debugPrint("storylyStoryDismissed"),
        storylyUserInteracted: (storyGroup, story, storyComponent) {
          debugPrint("userInteracted -> storyGroup: ${storyGroup.title}");
          debugPrint("userInteracted -> story: ${story.title}");
          debugPrint("userInteracted -> storyComponent: $storyComponent");
        },
      ),
    );
  }
}