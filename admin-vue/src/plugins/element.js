import Vue from 'vue'
import {
  Button, Form, FormItem, Input, Message, Container, Header, Aside, Main, Menu, Submenu,
  MenuItem, Breadcrumb, BreadcrumbItem, Card, Row, Col, Table, TableColumn, Switch, Tooltip,
  Pagination, Dialog, MessageBox, Tag, Tree, Select, Option, Cascader, Alert, Tabs, TabPane,
  Steps, Step, CheckboxGroup, Checkbox, Upload, Timeline, TimelineItem
} from 'element-ui'

Vue.use(Button, Form, FormItem, Input, Container, Header, Aside, Main, Menu, Submenu, MenuItem,
  Breadcrumb, BreadcrumbItem, Card, Row, Col, Table, TableColumn, Switch, Tooltip, Pagination, Dialog,
  Tag, Tree, Select, Option, Cascader, Alert, Tabs, TabPane, Step, Steps, CheckboxGroup, Checkbox,
  Upload, Timeline, TimelineItem)

Vue.prototype.$message = Message

Vue.prototype.$confirm = MessageBox.confirm
