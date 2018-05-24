# bilibili-for-app
仿哔哩哔哩移动端
应用架构：
XuanRecyclerView
========
- 一个开源哔哩哔哩移动端
- 采用MVP架构
- 基础架构采用 RxBaseActivity RxBaseFragment Fragmentation
- 使用 rxjava+rxcache+rxLifecycle+retrofit2+okhttp+eventbus+buffknife+glide 进行系统架构
- 使用自定义 RecyclerView  进行列表数据的填充 上拉加载下拉刷新功能 支持自定义header footer
- 全局采用沉浸式状态栏 支持颜色切换
- 布局文件采用 CoordinatorLayout AppBarLayout CollapsingToolbarLayout Toolbar进行伸缩状态栏
- 视频播放直播 弹幕库 采用哔哩哔哩开源控件
- 数据源取自哔哩哔哩客户端 
- 因时间关系，本项目只完成大部分主要功能，细节页面后续有空再补充

========

- Details of reference demo

- thanks start and fork


Image
========
<img src="https://github.com/xiansenxuan/bilibili-for-app/blob/master/images/" width = "320" height = "480" alt="sample"/>

Thanks

本项目仅供学习使用，禁止任何商业用途
========

```
Copyright 2015

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
