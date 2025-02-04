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
        // Do any additional setup after loading the view.
        self.storylyView.storylyInit = StorylyInit(storylyId: "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhY2NfaWQiOjc2MCwiYXBwX2lkIjo0MDUsImluc19pZCI6NDA0fQ.1AkqOy_lsiownTBNhVOUKc91uc9fDcAxfQZtpm3nj40")
        self.storylyView.rootViewController = self
        self.storylyView.delegate = self
    }
}

extension BasicViewController: StorylyDelegate {
    func storylyActionClicked(_ storylyView: StorylyView, rootViewController: UIViewController, story: Story) {
        let vc = UIViewController()
        vc.modalPresentationStyle = .overFullScreen
        vc.view.backgroundColor = .white
        
        let button = UIButton(type: .system)
        button.setTitle("Back", for: .normal)
        button.addTarget(self, action: #selector(dismissViewController), for: .touchUpInside)
        button.translatesAutoresizingMaskIntoConstraints = false
        
        vc.view.addSubview(button)
        NSLayoutConstraint.activate([
            button.centerXAnchor.constraint(equalTo: vc.view.centerXAnchor),
            button.centerYAnchor.constraint(equalTo: vc.view.centerYAnchor)
        ])
        rootViewController.topViewController()?.present(vc, animated: true)
    }
    
    
    @objc private func dismissViewController() {
        self.topViewController()?.dismiss(animated: true, completion: nil)
    }
}



extension UIViewController {
    func topViewController() -> UIViewController? {
        if let presentedViewController = presentedViewController {
            return presentedViewController.topViewController()
        }
        if let navigationController = self as? UINavigationController {
            return navigationController.visibleViewController?.topViewController()
        }
        if let tabBarController = self as? UITabBarController {
            return tabBarController.selectedViewController?.topViewController()
        }
        return self
    }
}
