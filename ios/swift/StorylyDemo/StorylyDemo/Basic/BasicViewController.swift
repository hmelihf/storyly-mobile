//
//  BasicViewController.swift
//  StorylyDemo
//
//  Created by Levent ORAL on 25.09.2019.
//  Copyright © 2019 App Samurai Inc. All rights reserved.
//

import UIKit
import Storyly

class BasicViewController: UIViewController {

    @IBOutlet weak var storylyView: StorylyView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        view.backgroundColor = .white
        
        self.storylyView.storylyInit = StorylyInit(storylyId: "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhY2NfaWQiOjc2MCwiYXBwX2lkIjo0MDUsImluc19pZCI6NDA0fQ.1AkqOy_lsiownTBNhVOUKc91uc9fDcAxfQZtpm3nj40")
        self.storylyView.rootViewController = self
        self.storylyView.delegate = self
    }
}

extension BasicViewController: StorylyDelegate {
    func storylyActionClicked(_ storylyView: StorylyView, rootViewController: UIViewController, story: Story) {
        let vc = UIViewController()
        vc.view.backgroundColor = .white
        vc.title = "Details"
        
        let button = UIBarButtonItem(title: "Close", style: .plain, target: self, action: #selector(dismissViewController))
        vc.navigationItem.leftBarButtonItem = button
        storylyView.pauseStory(animated: true)
        navigationController?.pushViewController(vc, animated: true)
    }
    
    @objc private func dismissViewController() {
        navigationController?.popViewController(animated: true)
        storylyView.resumeStory(animated: true)
    }
}
